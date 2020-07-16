/*     */ package xaero.common.gui.widget.loader;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Map;
/*     */ import javax.imageio.ImageIO;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.resources.IResource;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL30;
/*     */ import org.lwjgl.opengl.GLContext;
/*     */ import xaero.common.gui.widget.ImageWidgetBuilder;
/*     */ import xaero.common.gui.widget.Widget;
/*     */ import xaero.common.gui.widget.WidgetBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ImageWidgetLoader
/*     */   extends ScalableWidgetLoader
/*     */ {
/*     */   private static void download(BufferedOutputStream output, InputStream input) throws IOException {
/*  37 */     byte[] buffer = new byte[256];
/*     */     while (true) {
/*  39 */       int read = input.read(buffer, 0, buffer.length);
/*  40 */       if (read < 0)
/*     */         break; 
/*  42 */       output.write(buffer, 0, read);
/*     */     } 
/*  44 */     output.flush();
/*  45 */     input.close();
/*  46 */     output.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public Widget load(Map<String, String> parsedArgs) throws IOException {
/*  51 */     ImageWidgetBuilder builder = new ImageWidgetBuilder();
/*  52 */     commonLoad((WidgetBuilder)builder, parsedArgs);
/*  53 */     String image = parsedArgs.get("image");
/*  54 */     String image_url = parsedArgs.get("image_url");
/*  55 */     int textureId = 0;
/*  56 */     if (image != null) {
/*  57 */       builder.setImageId(image);
/*  58 */       textureId = GL11.glGenTextures();
/*  59 */       if (textureId <= 0)
/*  60 */         return null; 
/*  61 */       builder.setGlTexture(textureId);
/*  62 */       GlStateManager.func_179098_w();
/*  63 */       GlStateManager.func_179144_i(textureId);
/*  64 */       String tex_base_level = parsedArgs.get("tex_base_level");
/*  65 */       String tex_max_level = parsedArgs.get("tex_max_level");
/*  66 */       String tex_min_lod = parsedArgs.get("tex_min_lod");
/*  67 */       String tex_max_lod = parsedArgs.get("tex_max_lod");
/*  68 */       String tex_lod_bias = parsedArgs.get("tex_lod_bias");
/*  69 */       String tex_mag_filter = parsedArgs.get("tex_mag_filter");
/*  70 */       String tex_min_filter = parsedArgs.get("tex_min_filter");
/*  71 */       String tex_wrap_s = parsedArgs.get("tex_wrap_s");
/*  72 */       String tex_wrap_t = parsedArgs.get("tex_wrap_t");
/*  73 */       GL11.glTexParameteri(3553, 33084, (tex_base_level != null) ? Integer.parseInt(tex_base_level) : 0);
/*  74 */       GL11.glTexParameteri(3553, 33085, (tex_max_level != null) ? Integer.parseInt(tex_max_level) : 1);
/*  75 */       GL11.glTexParameterf(3553, 33082, (tex_min_lod != null) ? Float.parseFloat(tex_min_lod) : 0.0F);
/*  76 */       GL11.glTexParameterf(3553, 33083, (tex_max_lod != null) ? Float.parseFloat(tex_max_lod) : 1.0F);
/*  77 */       GL11.glTexParameterf(3553, 34049, (tex_lod_bias != null) ? Float.parseFloat(tex_lod_bias) : 0.0F);
/*  78 */       if (!(GLContext.getCapabilities()).OpenGL30)
/*  79 */         GL11.glTexParameteri(3553, 33169, 1); 
/*  80 */       GL11.glTexParameteri(3553, 10240, (tex_mag_filter != null) ? Integer.parseInt(tex_mag_filter) : 9728);
/*  81 */       GL11.glTexParameteri(3553, 10241, (tex_min_filter != null) ? Integer.parseInt(tex_min_filter) : 9729);
/*  82 */       GL11.glTexParameteri(3553, 10242, (tex_wrap_s != null) ? Integer.parseInt(tex_wrap_s) : 33071);
/*  83 */       GL11.glTexParameteri(3553, 10243, (tex_wrap_t != null) ? Integer.parseInt(tex_wrap_t) : 33071);
/*     */       
/*  85 */       File cacheFolder = new File("XaeroCache");
/*  86 */       Files.createDirectories(cacheFolder.toPath(), (FileAttribute<?>[])new FileAttribute[0]);
/*  87 */       ResourceLocation resourceLocation = new ResourceLocation("xaerobetterpvp", "gui/" + image + ".png");
/*  88 */       InputStream inputStream = null;
/*  89 */       BufferedImage bufferedImage = null;
/*     */       try {
/*  91 */         IResource resource = Minecraft.func_71410_x().func_110442_L().func_110536_a(resourceLocation);
/*  92 */         inputStream = resource.func_110527_b();
/*  93 */         bufferedImage = ImageIO.read(inputStream);
/*  94 */         inputStream.close();
/*  95 */       } catch (FileNotFoundException e) {
/*  96 */         System.out.println("Widget image not included in jar. Checking cache...");
/*  97 */         Path cacheFilePath = cacheFolder.toPath().resolve(image + ".cache");
/*  98 */         if (!Files.exists(cacheFilePath, new java.nio.file.LinkOption[0])) {
/*  99 */           System.out.println("Widget image not in cache. Downloading...");
/* 100 */           if (image_url == null) {
/* 101 */             System.out.println("No image URL.");
/* 102 */             GlStateManager.func_179144_i(0);
/* 103 */             GL11.glDeleteTextures(textureId);
/* 104 */             return null;
/*     */           } 
/* 106 */           URL url = new URL(image_url);
/* 107 */           HttpURLConnection conn = (HttpURLConnection)url.openConnection();
/* 108 */           conn.setConnectTimeout(900);
/* 109 */           conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
/* 110 */           InputStream input = conn.getInputStream();
/* 111 */           BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(cacheFilePath.toFile()));
/*     */           try {
/* 113 */             download(output, input);
/*     */           } finally {
/* 115 */             input.close();
/* 116 */             output.close();
/*     */           } 
/*     */         } 
/* 119 */         inputStream = new FileInputStream(cacheFilePath.toFile());
/* 120 */         bufferedImage = ImageIO.read(inputStream);
/* 121 */         inputStream.close();
/* 122 */       } catch (Throwable e) {
/* 123 */         if (inputStream != null)
/* 124 */           inputStream.close(); 
/* 125 */         throw e;
/*     */       } 
/* 127 */       if (bufferedImage == null) {
/* 128 */         GlStateManager.func_179144_i(0);
/* 129 */         GL11.glDeleteTextures(textureId);
/* 130 */         return null;
/*     */       } 
/* 132 */       int imageW = bufferedImage.getWidth();
/* 133 */       int imageH = bufferedImage.getHeight();
/* 134 */       ByteBuffer buffer = BufferUtils.createByteBuffer(imageW * imageH * 4);
/* 135 */       for (int y = 0; y < imageH; y++) {
/* 136 */         for (int x = 0; x < imageW; x++) {
/* 137 */           int color = bufferedImage.getRGB(x, y);
/* 138 */           buffer.putInt(color);
/*     */         } 
/* 140 */       }  buffer.flip();
/* 141 */       bufferedImage.flush();
/* 142 */       GL11.glTexImage2D(3553, 0, 6408, imageW, imageH, 0, 32993, 33639, buffer);
/* 143 */       if ((GLContext.getCapabilities()).OpenGL30)
/* 144 */         GL30.glGenerateMipmap(3553); 
/* 145 */       GlStateManager.func_179144_i(0);
/* 146 */       builder.setImageW(imageW);
/* 147 */       builder.setImageH(imageH);
/*     */     } 
/* 149 */     if (builder.validate())
/* 150 */       return builder.build(); 
/* 151 */     if (textureId > 0)
/* 152 */       GL11.glDeleteTextures(textureId); 
/* 153 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\gui\widget\loader\ImageWidgetLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */