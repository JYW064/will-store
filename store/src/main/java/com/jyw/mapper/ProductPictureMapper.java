package com.jyw.mapper;

import com.jyw.entity.ProductPicture;
import java.util.List;

import com.jyw.entity.ProductPictureExample;
import org.apache.ibatis.annotations.Param;

public interface ProductPictureMapper {
    int deleteByExample(ProductPictureExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProductPicture record);

    int insertSelective(ProductPicture record);

    List<ProductPicture> selectByExample(ProductPictureExample example);

    ProductPicture selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProductPicture record, @Param("example") ProductPictureExample example);

    int updateByExample(@Param("record") ProductPicture record, @Param("example") ProductPictureExample example);

    int updateByPrimaryKeySelective(ProductPicture record);

    int updateByPrimaryKey(ProductPicture record);
}