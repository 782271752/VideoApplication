package com.li.videoapplication.utils;

import com.li.videoapplication.entity.VideoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maple on 2014/10/26.
 */
public class CollectCheckUtil {
    public static boolean isCheck=false;

    /**
     *记录选中的 收藏作品
     */
    public static List<VideoEntity> productList=new ArrayList<VideoEntity>();

    /**
     * 添加选中的收藏作品
     * @param collectProduct
     */
    public static void addCollectProduct(VideoEntity collectProduct){
        productList.add(collectProduct);
    }

    /**
     * 删除选中了之后又取消的收藏作品
     * @param collectProduct
     */
    public static void removeCollectProduce(VideoEntity collectProduct){
        for (int i=0;i<productList.size();i++){
            if (collectProduct.getPlayUrl().equals(productList.get(i).getPlayUrl())){
                productList.remove(i);
            }
        }
    }

    /**
     * 清空保存所有选中的收藏作品
     */
    public static void clearAllCollectProduce(){
        productList.clear();
    }

    /**
     * 返回选中的所有作品
     * @return
     */
    public static List<VideoEntity> getProductList(){
        return productList;
    }
}
