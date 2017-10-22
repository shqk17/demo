package cn.demo.ui.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.demo.service.NovelService;
import yhjp.bean.common.ResultBean;

@Controller
@RequestMapping("/novel")
public class NovelProduction {
	private static String NovelUrl = "http://www.bixia.org/50_50523";

	@Autowired
	private NovelService novelService;

	@RequestMapping("getTxt")
	@ResponseBody
	public void login(HttpServletRequest request, HttpServletResponse response) {
		InputStream novel = null;
		OutputStream out = null;
		try {
			novel = novelService.productNovel(NovelUrl, response);
			out = response.getOutputStream();
			int len = 0;
			byte[] buf = new byte[1024];
			while ((len = novel.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != novel) {
				try {
					novel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
