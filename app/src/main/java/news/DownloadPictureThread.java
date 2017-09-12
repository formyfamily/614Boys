package news;

import android.app.Activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by thinkpad on 2017/9/12.
 */

public class DownloadPictureThread extends Thread{
    private String urlStr;
    private Activity thisActivity;
    private ArrayList<String> currentList;
    private String id;
    private int count_;
    public DownloadPictureThread(String urlStr,Activity thisActivity,ArrayList<String> currentList,String id,int count_){
        this.urlStr = urlStr;
        this.thisActivity = thisActivity;
        this.currentList = currentList;
        this.id = id;
        this.count_ = count_;
    }
    @Override
    public void run() {                        // This thread is used to download pictures and add its local path to picturesLocal
        // TODO Auto-generated method stub
        try {
            //创建一个url对象
            URL url=new URL(urlStr);
            //打开URL对应的资源输入流
            InputStream is= url.openStream();
            //把InputStream转化成ByteArrayOutputStream
            ByteArrayOutputStream baos =new ByteArrayOutputStream();
            byte[] buffer =new byte[1024];
            int len;
            while ((len = is.read(buffer)) > -1 ) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            is.close();//关闭输入流
            //将ByteArrayOutputStream转化成InputStream
            is=new ByteArrayInputStream(baos.toByteArray());
            baos.close();
            //打开手机文件对应的输出流
            String imageName = count_ + ".jpg";
            File curDir = thisActivity.getExternalFilesDir(null);
            File targetDir = new File(curDir, "image/" + id);
            targetDir.mkdirs();
            File targetFile = new File(targetDir,imageName);
            FileOutputStream fos = new FileOutputStream(targetFile);
            byte[]buff=new byte[1024];
            int count=0;
            //将URL对应的资源下载到本地
            while ((count=is.read(buff))>0) {
                fos.write(buff, 0, count);
            }
            fos.flush();
            //关闭输入输出流
            is.close();
            fos.close();
            currentList.add(targetFile.getPath());
        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                File dataRoot = thisActivity.getFilesDir();
                File imageFolder = new File(dataRoot,"image");
                imageFolder.mkdirs();
                File imageNotFoundPicture = new File(imageFolder,"image-not-found.jpg");
                if (!imageNotFoundPicture.exists()){
                    InputStream is2 = thisActivity.getAssets().open("image-not-found.jpg");
                    byte[]buff2=new byte[1024];
                    int count2=0;
                    FileOutputStream fos2 = new FileOutputStream(imageNotFoundPicture);
                    while ((count2=is2.read(buff2))>0) {
                        fos2.write(buff2, 0, count2);
                    }
                    fos2.flush();
                    //关闭输入输出流
                    is2.close();
                    fos2.close();
                }
                currentList.add(imageNotFoundPicture.getPath());
            }catch(Exception f){
                f.printStackTrace();
            }
        }
    }
}
