package com.token.mangowallet.test;
import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Test {

	static String file1 = "C:\\Users\\Administrator\\Desktop\\新建文件夹\\组 87.png";
	static String file2 = "C:\\Users\\Administrator\\Desktop\\新建文件夹\\组 88.png";
	static String file3 = "C:\\Users\\Administrator\\Desktop\\新建文件夹\\组 89.png";

	public static void main(String[] args) {

		File f1 = new File(file1);
		File f2 = new File(file2);
		File f3 = new File(file3);


		MultipartBody.Builder m1 =new MultipartBody.Builder();
		m1.setType(MultipartBody.FORM);
		m1.addFormDataPart("file", f1.getName(), RequestBody.create(MediaType.parse("image/jpg"), f1));
		m1.addFormDataPart("file", f2.getName(), RequestBody.create(MediaType.parse("image/jpg"), f2));
		m1.addFormDataPart("file", f3.getName(), RequestBody.create(MediaType.parse("image/jpg"), f3));

		RequestBody requestBody = m1.build();
		Request.Builder RequestBuilder = new Request.Builder();
		RequestBuilder.url("https://api.coom.pub/file/upload");// 添加URL地址
		RequestBuilder.post(requestBody);
		Request request = RequestBuilder.build();

		OkHttpClient ok = new OkHttpClient();
		ok.newCall(request).enqueue(new Callback() {

			public void onResponse(Call call, Response res) throws IOException {
				String str = res.body().string();
				System.out.println(str);
			}

			public void onFailure(Call arg0, IOException arg1) {
				System.out.println("上传失败");
			}
		});

	}

}
