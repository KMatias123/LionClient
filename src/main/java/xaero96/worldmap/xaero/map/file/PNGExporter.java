/*     */ package xaero.map.file;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Hashtable;
/*     */ import java.util.stream.Stream;
/*     */ import javax.imageio.ImageIO;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL13;
/*     */ import org.lwjgl.opengl.GL30;
/*     */ import org.lwjgl.opengl.GLContext;
/*     */ import org.lwjgl.opengl.OpenGLException;
/*     */ import org.lwjgl.opengl.Util;
/*     */ import xaero.map.MapProcessor;
/*     */ import xaero.map.WorldMap;
/*     */ import xaero.map.graphics.ImprovedFramebuffer;
/*     */ import xaero.map.gui.GuiMap;
/*     */ import xaero.map.region.MapRegion;
/*     */ import xaero.map.region.MapTileChunk;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PNGExporter
/*     */ {
/*     */   private Path destinationPath;
/*     */   
/*     */   public PNGExporter(Path destinationPath) {
/*  36 */     this.destinationPath = destinationPath;
/*     */   }
/*     */   
/*     */   public void export() throws OpenGLException, IllegalArgumentException, IllegalAccessException {
/*  40 */     if (!MapProcessor.instance.getMapSaveLoad().isRegionDetectionComplete()) {
/*  41 */       System.out.println("Can't export the PNG just yet!");
/*     */       return;
/*     */     } 
/*  44 */     Hashtable<Integer, Hashtable<Integer, MapRegion>> map = MapProcessor.instance.getMapWorld().getCurrentDimension().getMapRegions();
/*  45 */     if (map.isEmpty())
/*     */       return; 
/*  47 */     Integer minX = null;
/*  48 */     Integer maxX = null;
/*  49 */     Integer minZ = null;
/*  50 */     Integer maxZ = null;
/*  51 */     for (Hashtable<Integer, MapRegion> column : map.values()) {
/*  52 */       for (MapRegion region : column.values()) {
/*  53 */         if (minX == null || region.getRegionX() < minX.intValue())
/*  54 */           minX = Integer.valueOf(region.getRegionX()); 
/*  55 */         if (maxX == null || region.getRegionX() > maxX.intValue())
/*  56 */           maxX = Integer.valueOf(region.getRegionX()); 
/*  57 */         if (minZ == null || region.getRegionZ() < minZ.intValue())
/*  58 */           minZ = Integer.valueOf(region.getRegionZ()); 
/*  59 */         if (maxZ == null || region.getRegionZ() > maxZ.intValue())
/*  60 */           maxZ = Integer.valueOf(region.getRegionZ()); 
/*     */       } 
/*  62 */     }  for (Hashtable<Integer, RegionDetection> column : (Iterable<Hashtable<Integer, RegionDetection>>)MapProcessor.instance.getMapWorld().getCurrentDimension().getDetectedRegions().values()) {
/*  63 */       for (RegionDetection regionDetection : column.values()) {
/*  64 */         if (minX == null || regionDetection.getRegionX() < minX.intValue())
/*  65 */           minX = Integer.valueOf(regionDetection.getRegionX()); 
/*  66 */         if (maxX == null || regionDetection.getRegionX() > maxX.intValue())
/*  67 */           maxX = Integer.valueOf(regionDetection.getRegionX()); 
/*  68 */         if (minZ == null || regionDetection.getRegionZ() < minZ.intValue())
/*  69 */           minZ = Integer.valueOf(regionDetection.getRegionZ()); 
/*  70 */         if (maxZ == null || regionDetection.getRegionZ() > maxZ.intValue())
/*  71 */           maxZ = Integer.valueOf(regionDetection.getRegionZ()); 
/*     */       } 
/*  73 */     }  int exportWidthInRegions = maxX.intValue() - minX.intValue() + 1;
/*  74 */     int exportHeightInRegions = maxZ.intValue() - minZ.intValue() + 1;
/*  75 */     float scale = (exportWidthInRegions * exportHeightInRegions < 400) ? 1.0F : (float)Math.sqrt(400.0D / (exportWidthInRegions * exportHeightInRegions));
/*  76 */     float regionSize = 512.0F * scale;
/*     */     
/*  78 */     int exportWidth = (int)(exportWidthInRegions * regionSize);
/*  79 */     int exportHeight = (int)(exportHeightInRegions * regionSize);
/*  80 */     int maxTextureSize = GlStateManager.func_187397_v(3379);
/*  81 */     Util.checkGLError();
/*     */     
/*  83 */     int frameWidth = Math.min(1024, Math.min(maxTextureSize, exportWidth));
/*  84 */     int frameHeight = Math.min(1024, Math.min(maxTextureSize, exportHeight));
/*     */     
/*  86 */     int horizontalFrames = (int)Math.ceil(exportWidth / frameWidth);
/*  87 */     int verticalFrames = (int)Math.ceil(exportHeight / frameHeight);
/*  88 */     if (WorldMap.settings.debug) {
/*  89 */       System.out.println(String.format("Exporting PNG of size %dx%d using a framebuffer of size %dx%d.", new Object[] { Integer.valueOf(exportWidth), Integer.valueOf(exportHeight), Integer.valueOf(frameWidth), Integer.valueOf(frameHeight) }));
/*     */     }
/*  91 */     ImprovedFramebuffer exportFrameBuffer = new ImprovedFramebuffer(frameWidth, frameHeight, false);
/*  92 */     ByteBuffer frameDataBuffer = BufferUtils.createByteBuffer(frameWidth * frameHeight * 4);
/*  93 */     int[] bufferArray = new int[frameWidth * frameHeight];
/*  94 */     BufferedImage image = new BufferedImage(exportWidth, exportHeight, 1);
/*  95 */     if (exportFrameBuffer.field_147616_f == -1) {
/*  96 */       System.out.println("Can't export PNG because FBOs are not supported.");
/*     */       
/*     */       return;
/*     */     } 
/* 100 */     GlStateManager.func_179123_a();
/* 101 */     GlStateManager.func_179140_f();
/* 102 */     GlStateManager.func_179128_n(5889);
/* 103 */     GlStateManager.func_179094_E();
/* 104 */     GlStateManager.func_179096_D();
/* 105 */     GlStateManager.func_179130_a(0.0D, frameWidth, 0.0D, frameHeight, 0.0D, 1000.0D);
/* 106 */     GlStateManager.func_179128_n(5888);
/* 107 */     GlStateManager.func_179094_E();
/*     */     
/* 109 */     GlStateManager.func_179096_D();
/* 110 */     GlStateManager.func_179129_p();
/* 111 */     exportFrameBuffer.func_147610_a(true);
/*     */     
/* 113 */     GlStateManager.func_179094_E();
/* 114 */     GlStateManager.func_179152_a(scale, scale, 1.0F);
/* 115 */     for (int i = 0; i < horizontalFrames; i++) {
/* 116 */       for (int j = 0; j < verticalFrames; j++) {
/* 117 */         GlStateManager.func_179144_i(0);
/* 118 */         GlStateManager.func_179082_a(0.0F, 0.0F, 0.0F, 1.0F);
/* 119 */         GlStateManager.func_179086_m(16640);
/* 120 */         GlStateManager.func_179094_E();
/*     */         
/* 122 */         float frameLeft = minX.intValue() * 512.0F + (i * frameWidth) / scale;
/* 123 */         float frameRight = minX.intValue() * 512.0F + ((i + 1) * frameWidth) / scale;
/* 124 */         float frameTop = minZ.intValue() * 512.0F + (j * frameHeight) / scale;
/* 125 */         float frameBottom = minZ.intValue() * 512.0F + ((j + 1) * frameHeight) / scale;
/*     */         
/* 127 */         int minTileChunkX = (int)Math.floor(frameLeft) >> 6;
/* 128 */         int maxTileChunkX = (int)Math.floor(frameRight) >> 6;
/* 129 */         int minTileChunkZ = (int)Math.floor(frameTop) >> 6;
/* 130 */         int maxTileChunkZ = (int)Math.floor(frameBottom) >> 6;
/* 131 */         int minRegionX = minTileChunkX >> 3;
/* 132 */         int minRegionZ = minTileChunkZ >> 3;
/* 133 */         int maxRegionX = maxTileChunkX >> 3;
/* 134 */         int maxRegionZ = maxTileChunkZ >> 3;
/*     */         
/* 136 */         GlStateManager.func_179109_b(-frameLeft, -frameTop, 0.0F);
/*     */         
/* 138 */         for (int regionX = minRegionX; regionX <= maxRegionX; regionX++) {
/* 139 */           for (int regionZ = minRegionZ; regionZ <= maxRegionZ; regionZ++) {
/* 140 */             boolean specialRegion = false;
/* 141 */             MapRegion region = MapProcessor.instance.getMapRegion(regionX, regionZ, false);
/* 142 */             if (region == null || (region.getLoadState() < 4 && (!region.isBeingWritten() || region.getLoadState() != 2))) {
/* 143 */               File cacheFile = null;
/* 144 */               if (region != null) {
/* 145 */                 cacheFile = region.getCacheFile();
/* 146 */               } else if (MapProcessor.instance.regionExists(regionX, regionZ)) {
/* 147 */                 cacheFile = MapProcessor.instance.getRegionDetection(regionX, regionZ).getCacheFile();
/* 148 */               }  if (cacheFile == null)
/*     */                 continue; 
/* 150 */               region = new MapRegion("png", null, regionX, regionZ);
/* 151 */               region.setCacheFile(cacheFile);
/* 152 */               region.loadCacheTextures();
/* 153 */               specialRegion = true;
/*     */             } 
/* 155 */             for (int localChunkX = 0; localChunkX < 8; localChunkX++) {
/* 156 */               for (int localChunkZ = 0; localChunkZ < 8; localChunkZ++)
/* 157 */               { MapTileChunk tileChunk = region.getChunk(localChunkX, localChunkZ);
/* 158 */                 if (tileChunk == null)
/*     */                   continue; 
/* 160 */                 if (tileChunk.getX() < minTileChunkX || tileChunk.getX() > maxTileChunkX || tileChunk.getZ() < minTileChunkZ || tileChunk.getZ() > maxTileChunkZ) {
/* 161 */                   if (specialRegion)
/* 162 */                     tileChunk.deleteBuffers(); 
/*     */                   continue;
/*     */                 } 
/* 165 */                 if (specialRegion) {
/* 166 */                   tileChunk.bindColorTexture(true, 9728);
/* 167 */                   if (tileChunk.getColorBufferIsCompressed()) {
/* 168 */                     GL13.glCompressedTexImage2D(3553, 0, tileChunk.getColorBufferFormat(), 64, 64, 0, tileChunk.getColorBuffer());
/*     */                   } else {
/* 170 */                     GL11.glTexImage2D(3553, 0, tileChunk.getColorBufferFormat(), 64, 64, 0, 32993, 32821, tileChunk.getColorBuffer());
/* 171 */                   }  if ((GLContext.getCapabilities()).OpenGL30)
/* 172 */                     GL30.glGenerateMipmap(3553); 
/* 173 */                   tileChunk.deleteBuffers();
/*     */                 } else {
/* 175 */                   if (tileChunk.getGlColorTexture() == -1)
/*     */                     continue; 
/* 177 */                   tileChunk.bindColorTexture(false, 9728);
/*     */                 } 
/* 179 */                 GlStateManager.func_187403_b(3553, 33082, 0.0F);
/* 180 */                 GlStateManager.func_187421_b(3553, 10241, 9987);
/*     */                 
/* 182 */                 GuiMap.renderTexturedModalRect((tileChunk.getX() * 64), (tileChunk.getZ() * 64), 64.0F, 64.0F);
/*     */                 
/* 184 */                 GlStateManager.func_187421_b(3553, 10241, 9984);
/* 185 */                 if (specialRegion)
/* 186 */                   GlStateManager.func_179150_h(tileChunk.getGlColorTexture());  continue; } 
/*     */             }  continue;
/*     */           } 
/* 189 */         }  GlStateManager.func_179121_F();
/*     */ 
/*     */         
/* 192 */         GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
/*     */         
/* 194 */         exportFrameBuffer.func_147612_c();
/* 195 */         frameDataBuffer.clear();
/* 196 */         GL11.glGetTexImage(3553, 0, 32993, 33639, frameDataBuffer);
/*     */         
/* 198 */         frameDataBuffer.asIntBuffer().get(bufferArray);
/*     */         
/* 200 */         int actualFrameWidth = Math.min(frameWidth, exportWidth - i * frameWidth);
/* 201 */         int actualFrameHeight = Math.min(frameHeight, exportHeight - j * frameWidth);
/* 202 */         image.setRGB(i * frameWidth, j * frameHeight, actualFrameWidth, actualFrameHeight, bufferArray, 0, frameWidth);
/*     */       } 
/* 204 */     }  GlStateManager.func_179121_F();
/* 205 */     GlStateManager.func_179099_b();
/*     */     
/* 207 */     exportFrameBuffer.func_147609_e();
/* 208 */     GlStateManager.func_179089_o();
/* 209 */     GlStateManager.func_179121_F();
/* 210 */     GlStateManager.func_179128_n(5889);
/* 211 */     GlStateManager.func_179121_F();
/* 212 */     GlStateManager.func_179128_n(5888);
/* 213 */     GlStateManager.func_179144_i(0);
/* 214 */     exportFrameBuffer.func_147608_a();
/* 215 */     MapProcessor.instance.getBufferDeallocator().deallocate(frameDataBuffer, WorldMap.settings.debug);
/*     */     
/* 217 */     Stream<Path> exports = null;
/*     */     try {
/* 219 */       Files.createDirectories(this.destinationPath, (FileAttribute<?>[])new FileAttribute[0]);
/* 220 */       int exportNumber = 1;
/* 221 */       exports = Files.list(this.destinationPath);
/* 222 */       if (exports == null)
/*     */         return; 
/* 224 */       Object[] exportsArray = exports.toArray();
/* 225 */       for (Object o : exportsArray) {
/* 226 */         Path path = (Path)o;
/* 227 */         if (path.getFileName().toString().endsWith("png")) {
/*     */           
/*     */           try {
/* 230 */             int currentNumber = Integer.parseInt(path.getFileName().toString().split("\\.")[0].split("_")[1]);
/* 231 */             if (currentNumber >= exportNumber)
/* 232 */               exportNumber = currentNumber + 1; 
/* 233 */           } catch (Exception e) {}
/*     */         }
/*     */       } 
/*     */       
/* 237 */       ImageIO.write(image, "png", this.destinationPath.resolve("export_" + exportNumber + ".png").toFile());
/* 238 */     } catch (IOException e1) {
/* 239 */       System.out.println("Failed to export PNG: ");
/* 240 */       e1.printStackTrace();
/*     */     } finally {
/* 242 */       if (exports != null)
/* 243 */         exports.close(); 
/* 244 */       image.flush();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\file\PNGExporter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */