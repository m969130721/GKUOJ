package com.oj.gkuoj.rest.common;

import com.oj.gkuoj.common.RestResponseEnum;
import com.oj.gkuoj.entity.User;
import com.oj.gkuoj.response.RestResponseVO;
import com.oj.gkuoj.service.FileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author m969130721@163.com
 * @date 19-1-17 下午1:24
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;


    /**
     * 　MD上传图片
     *
     * @param guid
     * @param multipartFile
     * @param userDetails
     * @return success  message url
     */
    @ResponseBody
    @RequestMapping(value = "/uploadImageByMD", method = RequestMethod.POST)
    public Map uploadImageByMD(String guid, @RequestParam(value = "editormd-image-file", required = false) MultipartFile multipartFile,
                           @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> map = new HashMap<>();
        if (userDetails == null) {
            map.put("success", 0);
            map.put("message", "上传失败，请先登录");
        }
        RestResponseVO restResponseVO = fileService.uploadImageByMD(multipartFile, guid,"ming");

        if (restResponseVO.isSuccess()) {
            map.put("success", 1);
            map.put("message", "上传成功");
            map.put("url", restResponseVO.getData());
        } else {
            map.put("success", 0);
            map.put("message", restResponseVO.getMsg());
        }
        return map;
    }


    /**
     * 　普通图片上传
     *
     * @param multipartFile
     * @param userDetails
     * @return success  message url
     */
    @ResponseBody
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public RestResponseVO uploadImage(@RequestParam(value = "file", required = false) MultipartFile multipartFile,
                               @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.UNAUTHORIZED);
        }
        User user = (User) userDetails;
       return fileService.uploadImage(multipartFile,user.getUsername());
    }




    @RequestMapping("/get")
    public RestResponseVO<byte[]> get(String token, String path) {
        //todo token
        if (StringUtils.isBlank(token)) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.TOKEN_ERROR);
        }
        return fileService.get(path);
    }


    @GetMapping("/getTestcaseInput")
    @ResponseBody
    public RestResponseVO getTestcaseInput(HttpServletResponse response,Integer problemId, Integer num,
                                 @AuthenticationPrincipal UserDetails userDetails){
        if (userDetails == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.UNAUTHORIZED);
        }
        fileService.getTestcaseInput(response,problemId,num);
        return RestResponseVO.createBySuccess();
    }

    @GetMapping("/getTestcaseOutput")
    public RestResponseVO getTestcaseOutput(HttpServletResponse response,Integer problemId, Integer num,@AuthenticationPrincipal UserDetails userDetails){
        if (userDetails == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.UNAUTHORIZED);
        }
        fileService.getTestcaseOutput(response,problemId,num);
        return RestResponseVO.createBySuccess();
    }

}
