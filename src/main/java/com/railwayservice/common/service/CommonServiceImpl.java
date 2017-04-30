package com.railwayservice.common.service;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.common.dao.DictionaryDao;
import com.railwayservice.common.dao.ImageInfoDao;
import com.railwayservice.common.dao.LocationDao;
import com.railwayservice.common.entity.Dictionary;
import com.railwayservice.common.entity.ImageInfo;
import com.railwayservice.common.entity.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务服务实现类。
 *
 * @author Ewing
 */
@Service
@PropertySource("classpath:common.properties")
public class CommonServiceImpl implements CommonService {
    private final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    private DictionaryDao dictionaryDao;
    private LocationDao locationDao;
    private ImageInfoDao imageInfoDao;

    // 图片保存相对路径
    public static final int IMAGE_MAX_WIDTH = 800;
    public static final int IMAGE_MAX_HEIGHT = 800;
    public static final String IMAGE_SAVE_FORMAT = "jpg";

    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.imageBucket}")
    private String imageBucket;
    @Value("${oss.imageDomain}")
    private String imageDomain;

    private OSSClient ossClient;

    @PostConstruct
    public void initOssClient() {
        try {
            ClientConfiguration conf = new ClientConfiguration();
            conf.setMaxConnections(1024);
            conf.setSocketTimeout(10000);
            conf.setMaxErrorRetry(5);
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret, conf);
            String classpath = CommonServiceImpl.class.getClassLoader().getResource("").getPath();
            String topDirectory = classpath.replaceAll("(.+)/[^/]+/[^/]+/", "$1");
            File tempFile = new File(topDirectory + "/images/");
            if (!tempFile.exists()) tempFile.mkdirs();
        } catch (Exception e) {
            logger.error("初始化阿里云OSS客户端失败：", e);
        }
    }

    @Autowired
    public void setDictionaryDao(DictionaryDao dictionaryDao) {
        this.dictionaryDao = dictionaryDao;
    }

    @Autowired
    public void setLocationDao(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    @Autowired
    public void setImageInfoDao(ImageInfoDao imageInfoDao) {
        this.imageInfoDao = imageInfoDao;
    }

    /**
     * 根据类型获取字典值。
     */
    @Override
    public List<Dictionary> findDictionaryByType(String type) {
        return dictionaryDao.findByTypeOrderByValue(type);
    }

    /**
     * 获取地理位置信息。
     */
    @Override
    public List<Location> findLocationByParentId(String parentId) {
        return locationDao.findByParentIdOrderBySort(parentId);
    }

    /**
     * 根据ID获取图片。
     */
    @Override
    public ImageInfo getImageInfoById(String imageId) {
        return imageInfoDao.findOne(imageId);
    }

    /**
     * 添加图片的方法。
     */
    @Override
    @Transactional
    public ImageInfo addImage(InputStream inputStream) {
        if (inputStream == null)
            throw new AppException("图片输入流不能为空！");
        logger.info("业务服务实现层：添加图片的方法：");
        String tempName = null;
        File tempFile = null;
        try {
            // 保存图片信息，格式，存储目录等。
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setCreateDate(new Date());
            imageInfo = imageInfoDao.save(imageInfo);
            imageInfo.setFormat(IMAGE_SAVE_FORMAT);
            String imageKey = imageInfo.getImageId() + "." + IMAGE_SAVE_FORMAT;
            imageInfo.setUrl(imageDomain + "/" + imageKey);
            imageInfoDao.save(imageInfo);

            // 设置内容类型。
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType("image/jpeg");
            // 原图上传到阿里OSS待处理
            tempName = "temp" + imageInfo.getImageId();
            ossClient.putObject(imageBucket, tempName, inputStream, meta);

            // 取Web根目录，只适用于 Web根目录/子目录/类目录 结构，否则取的是类目录。
            String classpath = CommonServiceImpl.class.getClassLoader().getResource("").getPath();
            String topDirectory = classpath.replaceAll("(.+)/[^/]+/[^/]+/", "$1");
            tempFile = new File(topDirectory + "/images/" + tempName);
            // 使用阿里OSS处理图片。
            String style = "image/resize,w_" + IMAGE_MAX_WIDTH + ",h_" + IMAGE_MAX_HEIGHT + ",m_lfit/format,jpg";
            GetObjectRequest request = new GetObjectRequest(imageBucket, tempName);
            request.setProcess(style);
            ossClient.getObject(request, tempFile);

            // 保存处理后的图片，删除原图以保证访问速度。
            ossClient.putObject(imageBucket, imageKey, tempFile, meta);
            ossClient.deleteObject(imageBucket, tempName);
            tempFile.delete();
            return imageInfo;
        } catch (Exception e) {
            try {
                if (tempName != null)
                    ossClient.deleteObject(imageBucket, tempName);
            } catch (Exception et) {
                logger.error("异常时清理阿里云临时文件失败：" + tempName);
            }
            try {
                if (tempFile != null)
                    tempFile.delete();
            } catch (Exception et) {
                logger.error("异常时清理本地临时文件失败：" + tempFile.getPath());
            }
            throw new AppException("图片处理或存储异常！");
        }
    }

    /**
     * 删除图片的方法。
     */
    @Override
    @Transactional
    public void deleteImage(String imageId) {
        if (!StringUtils.hasText(imageId))
            throw new AppException("请选择要删除的图片！");
        logger.info("业务服务实现层：删除图片的方法： 图片ID：" + imageId);
        ImageInfo imageInfo = imageInfoDao.findOne(imageId);
        if (imageInfo != null) {
            imageInfoDao.delete(imageId);
            ossClient.deleteObject(imageBucket, imageId + "." + imageInfo.getFormat());
        } else {
            ossClient.deleteObject(imageBucket, imageId + ".jpg");
        }
    }

    @Override
    public String getImageUrl(String imageId) {
        logger.info("根据图片ID集合查询图片信息：");
        if (!StringUtils.hasText(imageId)) return null;
        logger.info("业务服务实现层：删除图片的方法： 图片ID：" + imageId);

        ImageInfo image = imageInfoDao.findOne(imageId);
        if (image == null) {
            return null;
        } else {
            return image.getUrl();
        }
    }

    @Override
    public Map<String, String> getImageUrls(List<String> imageIds) {
        logger.info("业务服务实现层：根据图片ID集合查询所有图片： 图片ID个数：" + imageIds.size());
        // 根据图片ID集合查询所有图片对象。
        List<ImageInfo> imageInfos = imageInfoDao.findAll(imageIds);
        // 存储图片对象到Map容器中。
        Map<String, String> imageUrls = new HashMap<>();
        for (ImageInfo image : imageInfos) {
            imageUrls.put(image.getImageId(), image.getUrl());
        }
        return imageUrls;
    }

}
