package com.mmall.controller.backend;


import com.google.common.collect.Maps;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;

import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;

import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;


@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;


    @ResponseBody
    @RequestMapping("save.do")
    public ServerResponse productSave(HttpServletRequest request, Product product){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken))
            return ServerResponse.createByErrorMessage("Can't get user's information!");
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Administrator login is needed!");
        }

        if(iUserService.checkAdminRole(user).isSuccess()){
            //Add product
            return iProductService.saveOrUpdateProduct(product);
        }else{
            return ServerResponse.createByErrorMessage("No privilege to operate");
        }
    }

    @ResponseBody
    @RequestMapping("set_status.do")
    public ServerResponse setSaleStatus(HttpServletRequest request, Integer productId, Integer status){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken))
            return ServerResponse.createByErrorMessage("Can't get user's information!");
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Administrator login is needed!");
        }

        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.setSaleStatus(productId, status);
        }else{
            return ServerResponse.createByErrorMessage("No privilege to operate");
        }
    }

    @ResponseBody
    @RequestMapping("detail.do")
    public ServerResponse getDetail(HttpServletRequest request, Integer productId){
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isEmpty(loginToken))
//            return ServerResponse.createByErrorMessage("Can't get user's information!");
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);
//        if (user == null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Administrator login is needed!");
//        }
//
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            return iProductService.manageProductDetail(productId);
//        }else{
//            return ServerResponse.createByErrorMessage("No privilege to operate");
//        }

        return iProductService.manageProductDetail(productId);
    }

    @ResponseBody
    @RequestMapping("list.do")
    public ServerResponse getList(HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,  @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isEmpty(loginToken))
//            return ServerResponse.createByErrorMessage("Can't get user's information!");
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);
//
//        if (user == null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Administrator login is needed!");
//        }
//
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            return iProductService.getProductList(pageNum, pageSize);
//        }else{
//            return ServerResponse.createByErrorMessage("No privilege to operate");
//        }

        return iProductService.getProductList(pageNum, pageSize);
    }

    @ResponseBody
    @RequestMapping("search.do")
    public ServerResponse productSearch(HttpServletRequest request, String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,  @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isEmpty(loginToken))
//            return ServerResponse.createByErrorMessage("Can't get user's information!");
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);
//        if (user == null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Administrator login is needed!");
//        }
//
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            return iProductService.searchProduct(productName, productId, pageNum, pageSize);
//        }else{
//            return ServerResponse.createByErrorMessage("No privilege to operate");
//        }

        return iProductService.searchProduct(productName, productId, pageNum, pageSize);
    }

    @ResponseBody
    @RequestMapping("upload.do")
    public ServerResponse upload(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request){
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isEmpty(loginToken))
//            return ServerResponse.createByErrorMessage("Can't get user's information!");
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);
//        if (user == null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Administrator login is needed!");
//        }
//
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            String path = request.getSession().getServletContext().getRealPath("upload");
//            String targetFileName = iFileService.upload(file,path);
//            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
//
//            Map fileMap = Maps.newHashMap();
//            fileMap.put("uri",targetFileName);
//            fileMap.put("url",url);
//
//            return ServerResponse.createBySuccess(fileMap);
//        }else{
//            return ServerResponse.createByErrorMessage("No privilege to operate");
//        }

        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri",targetFileName);
        fileMap.put("url",url);

        return ServerResponse.createBySuccess(fileMap);
    }

    @ResponseBody
    @RequestMapping("richtext_img_upload.do")
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isEmpty(loginToken))
//            return resultMap;
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);
//        if (user == null){
//            resultMap.put("success", false);
//            resultMap.put("msg", "Please Login administrator");
//            return resultMap;
//        }
//
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            String path = request.getSession().getServletContext().getRealPath("upload");
//            String targetFileName = iFileService.upload(file,path);
//            if (StringUtils.isBlank(targetFileName)){
//                resultMap.put("success", false);
//                resultMap.put("msg", "Upload failed");
//                return resultMap;
//            }
//
//            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
//
//            resultMap.put("success", true);
//            resultMap.put("msg","uploaded successfully!");
//            resultMap.put("file_path",url);
//
//            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
//            return resultMap;
//        }else{
//            resultMap.put("success", false);
//            resultMap.put("msg", "No privilege to operate");
//            return resultMap;
//        }

        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);
        if (StringUtils.isBlank(targetFileName)){
            resultMap.put("success", false);
            resultMap.put("msg", "Upload failed");
            return resultMap;
        }

        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

        resultMap.put("success", true);
        resultMap.put("msg","uploaded successfully!");
        resultMap.put("file_path",url);

        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
        return resultMap;
    }
}
