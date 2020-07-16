/*     */ package xaero.common.minimap;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*     */ import net.minecraft.util.SoundCategory;
/*     */ import net.minecraft.world.World;
/*     */ import xaero.common.IXaeroMinimap;
/*     */ import xaero.common.settings.ModSettings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MinimapRadar
/*     */ {
/*  23 */   public static final Color radarPlayers = new Color(255, 255, 255);
/*  24 */   public static final Color radarShadow = new Color(0, 0, 0);
/*     */   
/*     */   private IXaeroMinimap modMain;
/*     */   private ArrayList<Entity> players;
/*     */   private ArrayList<Entity> living;
/*     */   private ArrayList<Entity> hostile;
/*     */   private ArrayList<Entity> items;
/*     */   private ArrayList<Entity> entities;
/*     */   private ArrayList<Entity> updatingPlayers;
/*     */   private ArrayList<Entity> updatingHostile;
/*     */   private ArrayList<Entity> updatingLiving;
/*     */   private ArrayList<Entity> updatingItems;
/*     */   private ArrayList<Entity> updatingEntities;
/*     */   
/*     */   public MinimapRadar(IXaeroMinimap modMain) {
/*  39 */     this.modMain = modMain;
/*  40 */     this.players = new ArrayList<>();
/*  41 */     this.living = new ArrayList<>();
/*  42 */     this.hostile = new ArrayList<>();
/*  43 */     this.items = new ArrayList<>();
/*  44 */     this.entities = new ArrayList<>();
/*  45 */     this.updatingPlayers = new ArrayList<>();
/*  46 */     this.updatingHostile = new ArrayList<>();
/*  47 */     this.updatingLiving = new ArrayList<>();
/*  48 */     this.updatingItems = new ArrayList<>();
/*  49 */     this.updatingEntities = new ArrayList<>();
/*     */   }
/*     */   
/*     */   public void updateRadar(World world, EntityPlayer p, Entity renderEntity) {
/*  53 */     this.updatingPlayers.clear();
/*  54 */     this.updatingHostile.clear();
/*  55 */     this.updatingLiving.clear();
/*  56 */     this.updatingItems.clear();
/*  57 */     this.updatingEntities.clear();
/*  58 */     for (int i = 0; i < world.field_72996_f.size(); i++) {
/*     */       try {
/*  60 */         Entity e = world.field_72996_f.get(i);
/*  61 */         int type = 0;
/*  62 */         if (e instanceof EntityPlayer) {
/*  63 */           if (e != p && (!this.modMain.getSettings().getShowPlayers() || (!this.modMain.getSettings().getShowOtherTeam() && p.func_96124_cp() != ((EntityLivingBase)e).func_96124_cp())))
/*     */             continue; 
/*  65 */           type = 1;
/*  66 */         } else if (e.getEntityData().func_74764_b("hostileMinimap") ? e.getEntityData().func_74767_n("hostileMinimap") : (e instanceof net.minecraft.entity.monster.EntityMob || e instanceof net.minecraft.entity.monster.IMob || e.func_184176_by() == SoundCategory.HOSTILE)) {
/*  67 */           if (!this.modMain.getSettings().getShowHostile())
/*     */             continue; 
/*  69 */           type = 2;
/*  70 */         } else if (e instanceof net.minecraft.entity.EntityLiving) {
/*  71 */           if (!this.modMain.getSettings().getShowMobs())
/*     */             continue; 
/*  73 */           type = 3;
/*  74 */         } else if (e instanceof net.minecraft.entity.item.EntityItem) {
/*  75 */           if (!this.modMain.getSettings().getShowItems())
/*     */             continue; 
/*  77 */           type = 4;
/*  78 */         } else if (!this.modMain.getSettings().getShowOther()) {
/*     */           continue;
/*  80 */         }  double offx = e.field_70165_t - renderEntity.field_70165_t;
/*  81 */         double offy = e.field_70161_v - renderEntity.field_70161_v;
/*  82 */         double offh = renderEntity.field_70163_u - e.field_70163_u;
/*  83 */         double offheight2 = offh * offh;
/*  84 */         double offx2 = offx * offx;
/*  85 */         double offy2 = offy * offy;
/*  86 */         double maxDistance = 50625.0D / MinimapProcessor.instance.getMinimapZoom() * MinimapProcessor.instance.getMinimapZoom();
/*  87 */         if (offx2 <= maxDistance && offy2 <= maxDistance && offheight2 <= (
/*  88 */           (this.modMain.getSettings()).heightLimit * 
/*  89 */           (this.modMain.getSettings()).heightLimit))
/*     */         
/*  91 */         { ArrayList<Entity> typeList = this.updatingEntities;
/*  92 */           switch (type) {
/*     */             case 1:
/*  94 */               typeList = this.updatingPlayers;
/*     */               break;
/*     */             case 2:
/*  97 */               typeList = this.updatingHostile;
/*     */               break;
/*     */             case 3:
/* 100 */               typeList = this.updatingLiving;
/*     */               break;
/*     */             case 4:
/* 103 */               typeList = this.updatingItems;
/*     */               break;
/*     */           } 
/* 106 */           typeList.add(e);
/* 107 */           if ((this.modMain.getSettings()).entityAmount != 0 && typeList
/* 108 */             .size() >= 100 * (this.modMain.getSettings()).entityAmount)
/*     */             break;  } 
/* 110 */       } catch (Exception e) {}
/*     */       
/*     */       continue;
/*     */     } 
/* 114 */     ArrayList<Entity> backupPlayers = this.players;
/* 115 */     ArrayList<Entity> backupHostile = this.hostile;
/* 116 */     ArrayList<Entity> backupLiving = this.living;
/* 117 */     ArrayList<Entity> backupItems = this.items;
/* 118 */     ArrayList<Entity> backupEntities = this.entities;
/* 119 */     this.players = this.updatingPlayers;
/* 120 */     this.hostile = this.updatingHostile;
/* 121 */     this.living = this.updatingLiving;
/* 122 */     this.items = this.updatingItems;
/* 123 */     this.entities = this.updatingEntities;
/* 124 */     this.updatingPlayers = backupPlayers;
/* 125 */     this.updatingHostile = backupHostile;
/* 126 */     this.updatingLiving = backupLiving;
/* 127 */     this.updatingItems = backupItems;
/* 128 */     this.updatingEntities = backupEntities;
/*     */   }
/*     */   
/*     */   public double getEntityX(Entity e, float partial) {
/* 132 */     return e.field_70142_S + (e.field_70165_t - e.field_70142_S) * partial;
/*     */   }
/*     */   
/*     */   public double getEntityZ(Entity e, float partial) {
/* 136 */     return e.field_70136_U + (e.field_70161_v - e.field_70136_U) * partial;
/*     */   }
/*     */   
/*     */   public boolean shouldRenderEntity(Entity e) {
/* 140 */     return (!e.func_70093_af() && !e.func_82150_aj());
/*     */   }
/*     */   
/*     */   public int getPlayerTeamColour(EntityPlayer p) {
/* 144 */     int teamColour = -1;
/* 145 */     if (p.func_96124_cp() != null && ((ScorePlayerTeam)p.func_96124_cp()).func_96668_e() != null && ((ScorePlayerTeam)p.func_96124_cp()).func_96668_e().length() >= 2) {
/* 146 */       String prefix = ((ScorePlayerTeam)p.func_96124_cp()).func_96668_e();
/*     */       try {
/* 148 */         teamColour = (Minecraft.func_71410_x()).field_71466_p.func_175064_b(prefix.charAt(prefix.length() - 1));
/* 149 */       } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
/*     */     } 
/*     */     
/* 152 */     return teamColour;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEntityColour(EntityPlayer p, Entity e, double offh) {
/* 157 */     int color = ModSettings.COLORS[(this.modMain.getSettings()).otherColor];
/* 158 */     if (e instanceof EntityPlayer) {
/* 159 */       int teamColour = getPlayerTeamColour(p);
/* 160 */       int entityTeamColour = getPlayerTeamColour((EntityPlayer)e);
/* 161 */       if ((this.modMain.getSettings()).otherTeamColor != -1 && entityTeamColour != teamColour) {
/* 162 */         color = ModSettings.COLORS[(this.modMain.getSettings()).otherTeamColor];
/* 163 */       } else if ((this.modMain.getSettings()).playersColor != -1) {
/* 164 */         color = ModSettings.COLORS[(this.modMain.getSettings()).playersColor];
/*     */       }
/* 166 */       else if (entityTeamColour != -1) {
/* 167 */         color = entityTeamColour;
/*     */       } else {
/* 169 */         color = radarPlayers.hashCode();
/*     */       } 
/* 171 */     } else if (e instanceof net.minecraft.entity.monster.EntityMob || e instanceof net.minecraft.entity.monster.IMob) {
/* 172 */       color = ModSettings.COLORS[(this.modMain.getSettings()).hostileColor];
/* 173 */     } else if (e instanceof net.minecraft.entity.EntityLiving) {
/* 174 */       color = ModSettings.COLORS[(this.modMain.getSettings()).mobsColor];
/* 175 */     } else if (e instanceof net.minecraft.entity.item.EntityItem) {
/* 176 */       color = ModSettings.COLORS[(this.modMain.getSettings()).itemsColor];
/* 177 */     }  int l = color >> 16 & 0xFF;
/* 178 */     int i1 = color >> 8 & 0xFF;
/* 179 */     int j1 = color & 0xFF;
/* 180 */     double brightness = getEntityBrightness(offh);
/* 181 */     if (brightness < 1.0D) {
/* 182 */       l = (int)(l * brightness);
/* 183 */       if (l > 255)
/* 184 */         l = 255; 
/* 185 */       i1 = (int)(i1 * brightness);
/* 186 */       if (i1 > 255)
/* 187 */         i1 = 255; 
/* 188 */       j1 = (int)(j1 * brightness);
/* 189 */       if (j1 > 255)
/* 190 */         j1 = 255; 
/* 191 */       color = 0xFF000000 | l << 16 | i1 << 8 | j1;
/*     */     } 
/* 193 */     return color;
/*     */   }
/*     */   
/*     */   public double getEntityBrightness(double offh) {
/* 197 */     double level = (this.modMain.getSettings()).heightLimit - offh;
/* 198 */     if (level < 0.0D)
/* 199 */       level = 0.0D; 
/* 200 */     double brightness = 1.0D;
/* 201 */     if (level <= ((this.modMain.getSettings()).heightLimit / 2) && (this.modMain.getSettings()).showEntityHeight)
/* 202 */       brightness = level / (this.modMain.getSettings()).heightLimit; 
/* 203 */     return brightness;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Entity> getEntitiesIterator() {
/* 208 */     return this.entities.iterator();
/*     */   }
/*     */   
/*     */   public Iterator<Entity> getItemsIterator() {
/* 212 */     return this.items.iterator();
/*     */   }
/*     */   
/*     */   public Iterator<Entity> getLivingIterator() {
/* 216 */     return this.living.iterator();
/*     */   }
/*     */   
/*     */   public Iterator<Entity> getHostileIterator() {
/* 220 */     return this.hostile.iterator();
/*     */   }
/*     */   
/*     */   public Iterator<Entity> getPlayersIterator() {
/* 224 */     return this.players.iterator();
/*     */   }
/*     */ }


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\minimap\MinimapRadar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */