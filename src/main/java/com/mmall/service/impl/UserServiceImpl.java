package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.tools.jstat.Token;

import java.util.UUID;


@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);

        if (resultCount == 0){
            return ServerResponse.createByError("Username does not exist!");
        }

        //TODO: encrypt the password with MD5 algorithm
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null){
            return ServerResponse.createByError("Incorrect Password");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("Login successfully",user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> validateResponse = checkValid(user.getUsername(), Const.USERNAME);
        if (!validateResponse.isSuccess()){
            return validateResponse;
        }
        validateResponse = checkValid(user.getUsername(), Const.EMAIL);
        if (!validateResponse.isSuccess()){
            return validateResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        // Encrypt the password by MD5
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if (resultCount == 0){
            return ServerResponse.createByError("Registration failed!");
        }
        return ServerResponse.createBySuccess("Successfully registered!");
    }

    public ServerResponse<String> checkValid(String str, String type){
        if (StringUtils.isNotBlank(type)){
            //Start validating
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);

                if (resultCount > 0){
                    return ServerResponse.createByError("Username has existed!");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);

                if (resultCount > 0){
                    return ServerResponse.createByError("Email has existed!");
                }
            }
        } else {
            return ServerResponse.createByError("Incorrect arguments");
        }
        return ServerResponse.createBySuccess("Validated!");
    }

    public ServerResponse selectQuestion(String username){
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()){
            return ServerResponse.createByError("User doesn't exist!");
        }
        String question = userMapper.selectQuestionByUsername(username);
         if (StringUtils.isNotBlank(question)){
             return ServerResponse.createBySuccess(question);
         }
         return ServerResponse.createByError("Empty question returned.");
    }

    public ServerResponse<String> checkAnswer(String username, String question, String answer){
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByError("The answer is incorrect.");
    }

    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByError("Argument error, token missed!");
        }

        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()){
            return ServerResponse.createByError("User doesn't exist!");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);

        if(StringUtils.isBlank(token)){
            return ServerResponse.createByError("Token is expired or not valid!");
        }
        if(StringUtils.equals(forgetToken, token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);

            if(rowCount > 0){
                return ServerResponse.createBySuccess("Password successfully updated");
            }

        }else{
            return ServerResponse.createByError("Token is not matched!");
        }
        return ServerResponse.createByError("Password update failed!");
    }
}
