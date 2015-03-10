package com.li.videoapplication.utils;

import com.li.videoapplication.entity.DownloadVideo;

import java.util.ArrayList;
import java.util.List;

/**操作图片
 * Created by li on 2014/10/16.
 */
public class ImgCheckUtils {

    /**
     * 判断收藏页面的是否进入图片选择模式
     */
    public static boolean isCheckState=false;

    /**
     *记录选中的 收藏作品
     */
    public static List<DownloadVideo> productList=new ArrayList<DownloadVideo>();

    /**
     * 添加选中的收藏作品
     * @param collectProduct
     */
    public static void addCollectProduct(DownloadVideo collectProduct){
        productList.add(collectProduct);
    }

    /**
     * 删除选中了之后又取消的收藏作品
     * @param collectProduct
     */
    public static void removeCollectProduce(DownloadVideo collectProduct){
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
    public static List<DownloadVideo> getProductList(){
        return productList;
    }

}
