//package com.click.cn.util;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.media.MediaMetadataRetriever;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Environment;
//import android.support.annotation.NonNull;
//import android.support.v4.content.FileProvider;
//import android.text.TextUtils;
//import android.text.format.DateUtils;
//import android.util.Base64;
//import android.util.Log;
//
//
//
//import java.io.BufferedOutputStream;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.io.RandomAccessFile;
//import java.io.Writer;
//import java.text.DecimalFormat;
//import java.util.Date;
//
///**
// * Created by lenovo on 2016/6/29.
// */
//public class FileUtils {
//
//    private static final String TAG = FileUtils.class.getSimpleName();
//    //fixme:
//    public static final String FILE_PROVIDER = "com.vito.cloudoa.fileprovider";
//
//    public static String getDataPathDefaultAssets(Context ctx, String in_subpath) {
//        String newpath = ctx.getFilesDir() + in_subpath;
//        File newfile = new File(newpath);
//
//        if (newfile.exists())
//            return "file:/" + newpath;
//        else
//            return "assets:/" + in_subpath;
//    }
//
//    public static String getDataPathDefaultHttp(Context ctx, String in_subpath) {
//        return in_subpath;
//    }
//
//    public static void writeJsonDataToFile(Context ctx, String in_subpath, Object in_obj) {
//        String newpath = ctx.getFilesDir() + in_subpath;
//
//        String parentpath = ctx.getFilesDir() + "/json";
//        File pardir = new File(parentpath);
//        if (!pardir.exists())
//            pardir.mkdirs();
//
//        try {
//            File newfile = new File(newpath);
//            if (!newfile.exists()) {
//                boolean nf = newfile.createNewFile();
//            }
//            newfile = new File(newpath);
//
//            JsonFactory jsonFactory = new JsonFactory();
//            JsonGenerator jsonGenerator = null;
//
//            jsonGenerator = jsonFactory.createGenerator(newfile, JsonEncoding.UTF8);
//
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.writeValue(jsonGenerator, in_obj);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Gets the extension of a file name, like ".png" or ".jpg".
//     *
//     * @param uri
//     * @return Extension including the dot("."); "" if there is no extension;
//     * null if uri was null.
//     */
//    public static String getExtension(String uri) {
//        if (uri == null) {
//            return null;
//        }
//
//        int dot = uri.lastIndexOf(".");
//        if (dot >= 0) {
//            return uri.substring(dot);
//        } else {
//            // No extension.
//            return "";
//        }
//    }
//
//    /**
//     * @param filePath
//     * @param seek
//     * @param length
//     * @return
//     */
//    public static byte[] readFlieToByte(String filePath, int seek, int length) {
//        if (TextUtils.isEmpty(filePath)) {
//            return null;
//        }
//        File file = new File(filePath);
//        if (!file.exists()) {
//            return null;
//        }
//        if (length == -1) {
//            length = (int) file.length();
//        }
//
//        try {
//            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
//            byte[] bs = new byte[length];
//            randomAccessFile.seek(seek);
//            randomAccessFile.readFully(bs);
//            randomAccessFile.close();
//            return bs;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static int copyFile(File src, String filename, byte[] buffer) {
//        if (!src.exists()) {
//            return -1;
//        }
//        return copyFile(src.getAbsolutePath(), filename, buffer);
//    }
//
//    /**
//     * 拷贝文件
//     *
//     * @param fileDir
//     * @param fileName
//     * @param buffer
//     * @return
//     */
//    public static int copyFile(String fileDir, String fileName, byte[] buffer) {
//        if (buffer == null) {
//            return -2;
//        }
//
//        try {
//            File file = new File(fileDir);
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//            File resultFile = new File(file, fileName);
//            if (!resultFile.exists()) {
//                resultFile.createNewFile();
//            }
//            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
//                    new FileOutputStream(resultFile, true));
//            bufferedOutputStream.write(buffer);
//            bufferedOutputStream.flush();
//            bufferedOutputStream.close();
//            return 0;
//
//        } catch (Exception e) {
//        }
//        return -1;
//    }
//
//    /**
//     * 根据文件名和后缀 拷贝文件
//     *
//     * @param fileDir
//     * @param fileName
//     * @param ext
//     * @param buffer
//     * @return
//     */
//    public static int copyFile(String fileDir, String fileName, String ext,
//                               byte[] buffer) {
//        return copyFile(fileDir, fileName + ext, buffer);
//    }
//
//    /**
//     * 根据后缀名判断是否是图片文件
//     *
//     * @param type
//     * @return 是否是图片结果true or false
//     */
//    public static boolean isImage(String type) {
//        if (type != null
//                && (type.equals("jpg") || type.equals("gif")
//                || type.equals("png") || type.equals("jpeg")
//                || type.equals("bmp") || type.equals("wbmp")
//                || type.equals("ico") || type.equals("jpe"))) {
//            return true;
//        }
//        return false;
//    }
//
//    public static String getFileExt(String fileName) {
//
//        if (TextUtils.isEmpty(fileName)) {
//
//            return "";
//        }
//        return fileName.substring(fileName.lastIndexOf(".") + 1,
//                fileName.length());
//    }
//
//    public static String getVideoMsgUrl(String url) {
//
//        if (TextUtils.isEmpty(url)) {
//
//            return "";
//        }
//        return url.substring(url.lastIndexOf("_") + 1,
//                url.length());
//
//    }
//
//    @SuppressLint("NewApi")
//    public static Bitmap createVideoThumbnail(String filePath) {
//        Bitmap bitmap = null;
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        try {
//            // retriever.setMode(MediaMetadataRetriever.);
//            retriever.setDataSource(filePath);
//
//            bitmap = retriever.getFrameAtTime(1000);
//
//        } catch (Exception ex) {
//
//        } finally {
//            try {
//                retriever.release();
//
//            } catch (RuntimeException ex) {
//            }
//
//        }
//        return bitmap;
//
//    }
//
//    /**
//     * 转换成单位
//     *
//     * @param length
//     * @return
//     */
//    public static String formatFileLength(long length) {
//        if (length >> 30 > 0L) {
//            float sizeGb = Math.round(10.0F * (float) length / 1.073742E+009F) / 10.0F;
//            return sizeGb + " GB";
//        }
//        if (length >> 20 > 0L) {
//            return formatSizeMb(length);
//        }
//        if (length >> 9 > 0L) {
//            float sizekb = Math.round(10.0F * (float) length / 1024.0F) / 10.0F;
//            return sizekb + " KB";
//        }
//        return length + " B";
//    }
//
//    /**
//     * 转换成Mb单位
//     *
//     * @param length
//     * @return
//     */
//    public static String formatSizeMb(long length) {
//        float mbSize = Math.round(10.0F * (float) length / 1048576.0F) / 10.0F;
//        return mbSize + " MB";
//    }
//
//    /**
//     * 检查SDCARD是否可写
//     *
//     * @return
//     */
//    public static boolean checkExternalStorageCanWrite() {
//        try {
//            boolean mouted = Environment.getExternalStorageState().equals(
//                    Environment.MEDIA_MOUNTED);
//            if (mouted) {
//                boolean canWrite = new File(Environment
//                        .getExternalStorageDirectory().getAbsolutePath())
//                        .canWrite();
//                if (canWrite) {
//                    return true;
//                }
//            }
//        } catch (Exception e) {
//        }
//        return false;
//    }
//
//    /**
//     * 过滤字符串为空
//     *
//     * @param str
//     * @return
//     */
//    public static String nullAsNil(String str) {
//        if (str == null) {
//            return "";
//        }
//        return str;
//    }
//
//
//    /**
//     * 是否图片
//     *
//     * @param fileName
//     * @return
//     */
//    public static boolean isPic(String fileName) {
//        String lowerCase = nullAsNil(fileName).toLowerCase();
//        return lowerCase.endsWith(".bmp") || lowerCase.endsWith(".png")
//                || lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg")
//                || lowerCase.endsWith(".gif");
//    }
//
//    /**
//     * 是否压缩文件
//     *
//     * @param fileName
//     * @return
//     */
//    public static boolean isCompresseFile(String fileName) {
//        String lowerCase = nullAsNil(fileName).toLowerCase();
//        return lowerCase.endsWith(".rar") || lowerCase.endsWith(".zip")
//                || lowerCase.endsWith(".7z") || lowerCase.endsWith("tar")
//                || lowerCase.endsWith(".iso");
//    }
//
//    /**
//     * 是否音频
//     *
//     * @param fileName
//     * @return
//     */
//    public static boolean isAudio(String fileName) {
//        String lowerCase = nullAsNil(fileName).toLowerCase();
//        return lowerCase.endsWith(".mp3") || lowerCase.endsWith(".wma")
//                || lowerCase.endsWith(".mp4") || lowerCase.endsWith(".rm");
//    }
//
//    /**
//     * 是否文档
//     *
//     * @param fileName
//     * @return
//     */
//    public static boolean isDocument(String fileName) {
//        String lowerCase = nullAsNil(fileName).toLowerCase();
//        return lowerCase.endsWith(".doc") || lowerCase.endsWith(".docx")
//                || lowerCase.endsWith("wps");
//    }
//
//    /**
//     * 是否Pdf
//     *
//     * @param fileName
//     * @return
//     */
//    public static boolean isPdf(String fileName) {
//        return nullAsNil(fileName).toLowerCase().endsWith(".pdf");
//    }
//
//    /**
//     * 是否Excel
//     *
//     * @param fileName
//     * @return
//     */
//    public static boolean isXls(String fileName) {
//        String lowerCase = nullAsNil(fileName).toLowerCase();
//        return lowerCase.endsWith(".xls") || lowerCase.endsWith(".xlsx");
//    }
//
//    /**
//     * 是否文本文档
//     *
//     * @param fileName
//     * @return
//     */
//    public static boolean isTextFile(String fileName) {
//        String lowerCase = nullAsNil(fileName).toLowerCase();
//        return lowerCase.endsWith(".txt") || lowerCase.endsWith(".rtf");
//    }
//
//    /**
//     * 是否Ppt
//     *
//     * @param fileName
//     * @return
//     */
//    public static boolean isPPt(String fileName) {
//        String lowerCase = nullAsNil(fileName).toLowerCase();
//        return lowerCase.endsWith(".ppt") || lowerCase.endsWith(".pptx");
//    }
//
//    /**
//     * decode file length
//     *
//     * @param filePath
//     * @return
//     */
//    public static int decodeFileLength(String filePath) {
//        if (TextUtils.isEmpty(filePath)) {
//            return 0;
//        }
//        File file = new File(filePath);
//        if (!file.exists()) {
//            return 0;
//        }
//        return (int) file.length();
//    }
//
//    /**
//     * @param filePath
//     * @return
//     */
//    public static boolean checkFile(String filePath) {
//        if (TextUtils.isEmpty(filePath) || !(new File(filePath).exists())) {
//            return false;
//        }
//        return true;
//    }
//
//    public static String getMIMEType(File file) {
//
//        String type = "*/*";
//        String fName = file.getName();
//        //获取后缀名前的分隔符"."在fName中的位置。
//        int dotIndex = fName.lastIndexOf(".");
//        if (dotIndex < 0) {
//            return type;
//        }
//        /* 获取文件的后缀名*/
//        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
//        if (end == "") return type;
//        //在MIME和文件类型的匹配表中找到对应的MIME类型。
//        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
//            if (end.equals(MIME_MapTable[i][0]))
//                type = MIME_MapTable[i][1];
//        }
//        return type;
//    }
//
//    public static int getFileIconRid(String in_name) {
//        String iconname = "file_default";
//
//        if (in_name == null) {
//            iconname = "pic_99";
//        } else {
//            int dotIndex = in_name.lastIndexOf(".");
//            if (dotIndex > 0) {
//                String end = in_name.substring(dotIndex, in_name.length()).toLowerCase();
//                if (end != null && end != "") {
//                    for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
//                        if (end.equals(MIME_MapTable[i][0]) && MIME_MapTable[i].length >= 3)
//                            iconname = MIME_MapTable[i][2];
//                    }
//                }
//            }
//        }
//
//        int re = 0;
//        try {
//            re = R.drawable.class.getDeclaredField(
//                    iconname).getInt(null);
//        } catch (IllegalArgumentException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return re;
//    }
//
//    private static final String[][] MIME_MapTable = {
//            //{后缀名，MIME类型}
//            {".3gp", "video/3gpp", "pic_100"},
//            {".apk", "application/vnd.Android.package-archive", "pic_102"},
//            {".asf", "video/x-ms-asf", "pic_100"},
//            {".avi", "video/x-msvideo", "pic_100"},
//            {".bin", "application/octet-stream"},
//            {".bmp", "image/bmp", "pic_98"},
//            {".c", "text/plain"},
//            {".class", "application/octet-stream"},
//            {".conf", "text/plain"},
//            {".cpp", "text/plain"},
//            {".doc", "application/msword", "pic_96"},
//            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "pic_96"},
//            {".xls", "application/vnd.ms-excel", "pic_97"},
//            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "pic_97"},
//            {".exe", "application/octet-stream"},
//            {".gif", "image/gif", "pic_97"},
//            {".gtar", "application/x-gtar"},
//            {".gz", "application/x-gzip", "pic_101"},
//            {".h", "text/plain"},
//            {".htm", "text/html"},
//            {".html", "text/html"},
//            {".jar", "application/java-archive"},
//            {".java", "text/plain"},
//            {".jpeg", "image/jpeg", "pic_98"},
//            {".jpg", "image/jpeg", "pic_98"},
//            {".js", "application/x-JavaScript"},
//            {".log", "text/plain"},
//            {".m3u", "audio/x-mpegurl"},
//            {".m4a", "audio/mp4a-latm"},
//            {".m4b", "audio/mp4a-latm"},
//            {".m4p", "audio/mp4a-latm"},
//            {".m4u", "video/vnd.mpegurl"},
//            {".m4v", "video/x-m4v"},
//            {".mov", "video/quicktime"},
//            {".mp2", "audio/x-mpeg"},
//            {".mp3", "audio/x-mpeg"},
//            {".mp4", "video/mp4", "pic_100"},
//            {".mpc", "application/vnd.mpohun.certificate"},
//            {".mpe", "video/mpeg"},
//            {".mpeg", "video/mpeg", "pic_100"},
//            {".mpg", "video/mpeg", "pic_100"},
//            {".mpg4", "video/mp4", "pic_100"},
//            {".mpga", "audio/mpeg", "pic_100"},
//            {".msg", "application/vnd.ms-outlook"},
//            {".ogg", "audio/ogg"},
//            {".pdf", "application/pdf", "pic_104"},
//            {".png", "image/png", "pic_98"},
//            {".pps", "application/vnd.ms-powerpoint", "pic_103"},
//            {".ppt", "application/vnd.ms-powerpoint", "pic_103"},
//            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation", "pic_103"},
//            {".prop", "text/plain"},
//            {".rar", "application/x-rar-compressed", "pic_101"},
//            {".rc", "text/plain"},
//            {".rmvb", "audio/x-pn-realaudio", "pic_100"},
//            {".rtf", "application/rtf"},
//            {".sh", "text/plain"},
//            {".tar", "application/x-tar", "pic_101"},
//            {".tgz", "application/x-compressed", "pic_101"},
//            {".txt", "text/plain", "pic_105"},
//            {".wav", "audio/x-wav"},
//            {".wma", "audio/x-ms-wma"},
//            {".wmv", "audio/x-ms-wmv"},
//            {".wps", "application/vnd.ms-works"},
//            {".xml", "text/plain"},
//            {".z", "application/x-compress"},
//            {".zip", "application/x-zip-compressed", "pic_101"},
//            {"", "*/*"}
//    };
//
//    private static String FormetFileSize(long fileS) {
//        DecimalFormat df = new DecimalFormat("#.00");
//        String fileSizeString = "";
//        String wrongSize = "0B";
//        if (fileS == 0) {
//            return wrongSize;
//        }
//        if (fileS < 1024) {
//            fileSizeString = df.format((double) fileS) + "B";
//        } else if (fileS < 1048576) {
//            fileSizeString = df.format((double) fileS / 1024) + "KB";
//        } else if (fileS < 1073741824) {
//            fileSizeString = df.format((double) fileS / 1048576) + "MB";
//        } else {
//            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
//        }
//        return fileSizeString;
//    }
//
//
//    /**
//     * @param dir
//     * @param numDays
//     * @return
//     */
//    public static int clearCacheFolder(final File dir, final int numDays) {
//
//        int deletedFiles = 0;
//        if (dir != null) {
//            Log.i("Info", "dir:" + dir.getAbsolutePath());
//        }
//
//        if (dir != null && dir.isDirectory()) {
//            try {
//                for (File child : dir.listFiles()) {
//
//                    //first delete subdirectories recursively
//                    if (child.isDirectory()) {
//                        deletedFiles += clearCacheFolder(child, numDays);
//                    }
//
//                    //then delete the files and subdirectories in this dir
//                    //only empty directories can be deleted, so subdirs have been done first
//                    if (child.lastModified() < new Date().getTime() - numDays * DateUtils.DAY_IN_MILLIS) {
//                        Log.i(TAG, "file name:" + child.getName());
//                        if (child.delete()) {
//                            deletedFiles++;
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                Log.e("Info", String.format("Failed to clean the cache, error %s", e.getMessage()));
//            }
//        }
//        return deletedFiles;
//    }
//
//
//    static void clearCache(final Context context, final int numDays) {
//        Log.i("Info", String.format("Starting cache prune, deleting files older than %d days", numDays));
//        int numDeletedFiles = clearCacheFolder(context.getCacheDir(), numDays);
//        Log.i("Info", String.format("Cache pruning completed, %d files deleted", numDeletedFiles));
//    }
//
//    public static Uri getFileUri(Context context, String filePath) {
//        return getFileUri(context, new File(filePath));
//    }
//
//    /**
//     * 兼容api24 FileProvider
//     *
//     * @param file
//     * @return
//     */
//    public static Uri getFileUri(@NonNull Context context, @NonNull File file) {
//        Uri uri = null;
//        /*
//         * 只有targetSdkVersion指定了在24及其以上，ANDROID N的手机才需要fileProvider
//         */
//        if (context.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            uri = FileProvider.getUriForFile(context, FILE_PROVIDER, file);
//        } else {
//            uri = Uri.fromFile(file);
//        }
//        return uri;
//    }
//
//    public static boolean isExist(String path) {
//        if (TextUtils.isEmpty(path)) {
//            return false;
//        }
//        File file = new File(path);
//        return file.exists();
//    }
//
//    public static boolean createFile(String filePath) {
//        return createFile(false, filePath);
//    }
//
//    public static boolean createFile(boolean isForce, String filePath) {
//        File file = new File(filePath);
//        try {
//            if (file.exists() && isForce) {
//                file.delete();
//            }
//            if (!file.exists()) {
//                return file.createNewFile();
//            } else {
//                return true;
//            }
//
//        } catch (Exception e) {
//            Log.e(TAG, "create File Exception:" + e.toString());
//        }
//        return false;
//    }
//
//    public static void delete(String filePath) {
//        if (!isExist(filePath))
//            return;
//        try {
//
//            File file = new File(filePath);
//            if (file.isDirectory()) {
//                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
//                if (files == null || files.length <= 0) {
//                    file.delete();
//                    return;
//                }
//                for (File file1 : files) {
//                    delete(file1.getAbsolutePath()); // 把每个文件 用这个方法进行迭代
//                }
//                file.delete();
//            } else {
//                file.delete();
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "delete file:" + filePath + " exception, " + e.toString());
//        }
//    }
//
//    /**
//     * 内容写入文件中
//     *
//     * @param content
//     */
//    public synchronized static void write(String content, String filePath) {
//        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            return;
//        }
//        if (TextUtils.isEmpty(content)) {
//            return;
//        }
//        Writer writer = null;
//        try {
//            writer = new BufferedWriter(new OutputStreamWriter(
//                    new FileOutputStream(new File(filePath), true)));
//            writer.write(new String(content.getBytes(), "UTF-8"));
//            writer.write("\r\n");
//            writer.flush();
//            content = null;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (writer != null) {
//                try {
//                    writer.close();
//                } catch (IOException e) {
//                }
//                writer = null;
//            }
//
//        }
//    }
//
//    // todo:
//    public static String readAllContent(String filePath) {
//
//        StringBuilder result = new StringBuilder();
//        File file = new File(filePath);
//        if (file.exists() && file.isFile()) {
//
//            try {
//                FileInputStream input = new FileInputStream(file);
////                int length = input.available();
//                byte[] bytes = new byte[1024 * 8];
//                int len = -1;
//                while ((len = input.read(bytes)) != -1) {
//                    result.append(Base64.decode(bytes, 0, len, 0)); // todo: --- 编码？？？
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                result.delete(0, result.length());
//            } catch (IOException e) {
//                e.printStackTrace();
//                result.delete(0, result.length());
//            } finally {
//                return result.toString();
//            }
//        } else {
//            return "";
//        }
//
//    }
//
//    /**
//     * 根据全路径获取文件名
//     *
//     * @param localFilePath
//     * @return
//     */
//    public static String getFileName(@NonNull String localFilePath) {
//        int lastSeparatorIndex = localFilePath.lastIndexOf(File.separator);
//        String fileName = (lastSeparatorIndex == -1 || lastSeparatorIndex == localFilePath.length() - 1) ?
//                "" : localFilePath.substring(lastSeparatorIndex + 1);
//        return fileName;
//    }
//}
