package com.example.controller;

import com.example.config.VerificationCode;
import com.example.entity.Hr;
import com.example.entity.help.RespBean;
import com.example.service.HrService;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
public class LoginController {
    HrService hrService;

    public LoginController(HrService hrService) {
        this.hrService = hrService;
    }

    @GetMapping("/login")
    public RespBean login() {
        System.out.println();
        return RespBean.error("尚未登录,请登录");
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name"));
    }

    @GetMapping("/upload/**")
    public void upload(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String uri = request.getRequestURI();
        uri = uri.replaceFirst("upload/", "");
        uri = URLDecoder.decode(uri, "utf-8");
        File file = new File(uri);
        try (
                ServletOutputStream outputStream = response.getOutputStream();
                FileInputStream inputStream = new FileInputStream(file)
        ) {

            byte[] _1MB = new byte[1024 * 1024];
            while (inputStream.read(_1MB) != -1) {
                outputStream.write(_1MB);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/verifyCode")
    public void verifyCode(HttpSession session, HttpServletResponse response) throws IOException {
        VerificationCode verifyCode = new VerificationCode();
        BufferedImage image = verifyCode.getImage();
        String text = verifyCode.getText();
        session.setAttribute("verifyCode", text);
        System.out.println(text);
        VerificationCode.output(image, response.getOutputStream());
    }


    @PostMapping("/userFace/{id}")
    public RespBean updateUserFace(@RequestParam("file") MultipartFile file,
                                   @PathVariable("id") Integer id) {
        String dirPath = File.separator + "upload" + File.separator;
        String filename = file.getOriginalFilename();
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdirs();
        File saveFile = new File(dir.getAbsolutePath(), filename);
        Hr hr = new Hr();
        String userface = dirPath + saveFile.getPath();
        try {
            file.transferTo(saveFile);
            hr.setUserface(userface);
            hr.setId(id);
        } catch (IOException e) {
            return RespBean.error("上传错误,请重试或联系管理员");
        }

        if (hrService.updateInfo(hr) == 1) {
            return RespBean.ok("上传修改头像成功",userface);
        }
        return RespBean.error("上传修改头像失败");
    }
}
