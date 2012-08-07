package net.minecraft.src.nbxlite;

import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class RenderGlobal2 extends RenderGlobal{
    public static boolean texClouds = false;
    public static boolean opaqueFlatClouds = false;
    public static boolean sunriseColors = true;
    public static boolean sunriseAtNorth = false;

    protected Minecraft mc;
    protected RenderEngine renderEngine;
    protected World worldObj;
    protected int cloudOffsetX;

    /** The star GL Call list */
    protected int starGLCallList;

    /** OpenGL sky list */
    protected int glSkyList;

    /** OpenGL sky list 2 */
    protected int glSkyList2;

    FloatBuffer floatBuffer;

    public RenderGlobal2(Minecraft mc2, RenderEngine re){
        super(mc2, re);
        mc = mc2;
        renderEngine = re;
        floatBuffer = BufferUtils.createFloatBuffer(16);
        starGLCallList = ((Integer)mod_OldDays.getField(net.minecraft.src.RenderGlobal.class, this, 15));
        glSkyList = ((Integer)mod_OldDays.getField(net.minecraft.src.RenderGlobal.class, this, 16));
        glSkyList2 = ((Integer)mod_OldDays.getField(net.minecraft.src.RenderGlobal.class, this, 17));
        cloudOffsetX = 0;
    }

    public void updateClouds()
    {
        cloudOffsetX++;
    }

    /**
     * Changes the world reference in RenderGlobal
     */
    public void func_72732_a(World par1World)
    {
        super.func_72732_a(par1World);
        worldObj = par1World;
    }

    /**
     * Renders the sky with the partial tick time. Args: partialTickTime
     */
    public void renderSky(float par1)
    {
        if (mc.field_71441_e.worldProvider.worldType == 1)
        {
            GL11.glDisable(GL11.GL_FOG);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelper.disableStandardItemLighting();
            GL11.glDepthMask(false);
            renderEngine.bindTexture(renderEngine.getTexture("/misc/tunnel.png"));
            Tessellator tessellator = Tessellator.instance;

            for (int i = 0; i < 6; i++)
            {
                GL11.glPushMatrix();

                if (i == 1)
                {
                    GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 2)
                {
                    GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 3)
                {
                    GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 4)
                {
                    GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
                }

                if (i == 5)
                {
                    GL11.glRotatef(-90F, 0.0F, 0.0F, 1.0F);
                }

                tessellator.startDrawingQuads();
                tessellator.setColorOpaque_I(0x181818);
                tessellator.addVertexWithUV(-100D, -100D, -100D, 0.0D, 0.0D);
                tessellator.addVertexWithUV(-100D, -100D, 100D, 0.0D, 16D);
                tessellator.addVertexWithUV(100D, -100D, 100D, 16D, 16D);
                tessellator.addVertexWithUV(100D, -100D, -100D, 16D, 0.0D);
                tessellator.draw();
                GL11.glPopMatrix();
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            return;
        }

        if (!mc.field_71441_e.worldProvider.isSurfaceWorld())
        {
            return;
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Vec3 vec3d = worldObj.getSkyColor(mc.renderViewEntity, par1);
        float f = (float)vec3d.xCoord;
        float f1 = (float)vec3d.yCoord;
        float f2 = (float)vec3d.zCoord;

        if (mc.gameSettings.anaglyph)
        {
            float f3 = (f * 30F + f1 * 59F + f2 * 11F) / 100F;
            float f4 = (f * 30F + f1 * 70F) / 100F;
            float f5 = (f * 30F + f2 * 70F) / 100F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        GL11.glColor3f(f, f1, f2);
        Tessellator tessellator1 = Tessellator.instance;
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glColor3f(f, f1, f2);
        GL11.glCallList(glSkyList);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.disableStandardItemLighting();
        float af[] = worldObj.worldProvider.calcSunriseSunsetColors(worldObj.getCelestialAngle(par1), par1);
        if (!sunriseColors){
            af = null;
        }

        if (af != null)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glPushMatrix();
            GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(MathHelper.sin(worldObj.getCelestialAngleRadians(par1)) >= 0.0F ? 0.0F : 180F, 0.0F, 0.0F, 1.0F);
            if (!sunriseAtNorth){
                GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
            }
            float f6 = af[0];
            float f8 = af[1];
            float f11 = af[2];

            if (mc.gameSettings.anaglyph)
            {
                float f14 = (f6 * 30F + f8 * 59F + f11 * 11F) / 100F;
                float f17 = (f6 * 30F + f8 * 70F) / 100F;
                float f20 = (f6 * 30F + f11 * 70F) / 100F;
                f6 = f14;
                f8 = f17;
                f11 = f20;
            }

            tessellator1.startDrawing(6);
            tessellator1.setColorRGBA_F(f6, f8, f11, af[3]);
            tessellator1.addVertex(0.0D, 100D, 0.0D);
            int j = 16;
            tessellator1.setColorRGBA_F(af[0], af[1], af[2], 0.0F);

            for (int k = 0; k <= j; k++)
            {
                float f21 = ((float)k * (float)Math.PI * 2.0F) / (float)j;
                float f22 = MathHelper.sin(f21);
                float f23 = MathHelper.cos(f21);
                tessellator1.addVertex(f22 * 120F, f23 * 120F, -f23 * 40F * af[3]);
            }

            tessellator1.draw();
            GL11.glPopMatrix();
            GL11.glShadeModel(GL11.GL_FLAT);
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glPushMatrix();
        double d = 1.0F - worldObj.getRainStrength(par1);
        float f7 = 0.0F;
        float f9 = 0.0F;
        float f12 = 0.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, (float) d);
        GL11.glTranslatef(f7, f9, f12);
        if (!sunriseAtNorth){
            GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
        }else{
            GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
        }
        GL11.glRotatef(worldObj.getCelestialAngle(par1) * 360F, 1.0F, 0.0F, 0.0F);
        if (ODNBXlite.DayNight>0){
            float f15 = 30F;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderEngine.getTexture("/terrain/sun.png"));
            tessellator1.startDrawingQuads();
            tessellator1.addVertexWithUV(-f15, 100D, -f15, 0.0D, 0.0D);
            tessellator1.addVertexWithUV(f15, 100D, -f15, 1.0D, 0.0D);
            tessellator1.addVertexWithUV(f15, 100D, f15, 1.0D, 1.0D);
            tessellator1.addVertexWithUV(-f15, 100D, f15, 0.0D, 1.0D);
            tessellator1.draw();
            f15 = 20F;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderEngine.getTexture("/terrain/moon_phases.png"));
            int i18 = worldObj.getMoonPhase(par1);
            int l = i18 % 4;
            int i1 = (i18 / 4) % 2;
            float f24 = (float)(l + 0) / 4F;
            float f25 = (float)(i1 + 0) / 2.0F;
            float f26 = (float)(l + 1) / 4F;
            float f27 = (float)(i1 + 1) / 2.0F;
            tessellator1.startDrawingQuads();
            tessellator1.addVertexWithUV(-f15, -100D, f15, f26, f27);
            tessellator1.addVertexWithUV(f15, -100D, f15, f24, f27);
            tessellator1.addVertexWithUV(f15, -100D, -f15, f24, f25);
            tessellator1.addVertexWithUV(-f15, -100D, -f15, f26, f25);
            tessellator1.draw();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            float f18 = (float)(worldObj.getStarBrightness(par1) * d);

            if (f18 > 0.0F)
            {
                GL11.glColor4f(f18, f18, f18, f18);
                GL11.glCallList(starGLCallList);
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glPopMatrix();
        d = mc.field_71439_g.getPosition(par1).yCoord - worldObj.getHorizon();
        if (ODNBXlite.VoidFog<2){
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor3f(0.0F, 0.0F, 0.0F);
            if (d < 0.0D && ODNBXlite.VoidFog==0)
            {
                GL11.glPushMatrix();
                GL11.glTranslatef(0.0F, 12F, 0.0F);
                GL11.glCallList(glSkyList2);
                GL11.glPopMatrix();
                float f10 = 1.0F;
                float f13 = -(float)(d + 65D);
                float f16 = -f10;
                float f19 = f13;
                tessellator1.startDrawingQuads();
                tessellator1.setColorRGBA_I(0, 255);
                tessellator1.addVertex(-f10, f19, f10);
                tessellator1.addVertex(f10, f19, f10);
                tessellator1.addVertex(f10, f16, f10);
                tessellator1.addVertex(-f10, f16, f10);
                tessellator1.addVertex(-f10, f16, -f10);
                tessellator1.addVertex(f10, f16, -f10);
                tessellator1.addVertex(f10, f19, -f10);
                tessellator1.addVertex(-f10, f19, -f10);
                tessellator1.addVertex(f10, f16, -f10);
                tessellator1.addVertex(f10, f16, f10);
                tessellator1.addVertex(f10, f19, f10);
                tessellator1.addVertex(f10, f19, -f10);
                tessellator1.addVertex(-f10, f19, -f10);
                tessellator1.addVertex(-f10, f19, f10);
                tessellator1.addVertex(-f10, f16, f10);
                tessellator1.addVertex(-f10, f16, -f10);
                tessellator1.addVertex(-f10, f16, -f10);
                tessellator1.addVertex(-f10, f16, f10);
                tessellator1.addVertex(f10, f16, f10);
                tessellator1.addVertex(f10, f16, -f10);
                tessellator1.draw();
            }
        }

        if (worldObj.worldProvider.isSkyColored())
        {
            GL11.glColor3f(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
        }
        else if (ODNBXlite.VoidFog<4)
        {
            GL11.glColor3f(f, f1, f2);
        }

        if (ODNBXlite.VoidFog==0){
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, -(float)(d - 16D), 0.0F);
            GL11.glCallList(glSkyList2);
            GL11.glPopMatrix();
        }else if (ODNBXlite.VoidFog==1){
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, -(float)(Math.max(d, 1.0D) - 16D), 0.0F);
            GL11.glCallList(glSkyList2);
            GL11.glPopMatrix();
        }else if (ODNBXlite.VoidFog<4){
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glCallList(glSkyList2);
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
    }

    public void renderClouds(float par1)
    {
        if (!mc.field_71441_e.worldProvider.isSurfaceWorld())
        {
            return;
        }

        if (mc.gameSettings.fancyGraphics && !opaqueFlatClouds)
        {
            renderCloudsFancy(par1);
            return;
        }

        GL11.glDisable(GL11.GL_CULL_FACE);
        float f = (float)(mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY) * (double)par1);
        byte byte0 = 32;
        int i = (opaqueFlatClouds ? 1024 : 256) / byte0;
        Tessellator tessellator = Tessellator.instance;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderEngine.getTexture("/environment/clouds.png"));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Vec3 vec3d = worldObj.drawClouds(par1);
        float f1 = (float)vec3d.xCoord;
        float f2 = (float)vec3d.yCoord;
        float f3 = (float)vec3d.zCoord;

        if (mc.gameSettings.anaglyph)
        {
            float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
            float f6 = (f1 * 30F + f2 * 70F) / 100F;
            float f7 = (f1 * 30F + f3 * 70F) / 100F;
            f1 = f4;
            f2 = f6;
            f3 = f7;
        }

        float f5 = 0.0004882813F;
        double d = (float)cloudOffsetX + par1;
        double d1 = mc.renderViewEntity.prevPosX + (mc.renderViewEntity.posX - mc.renderViewEntity.prevPosX) * (double)par1 + d * 0.029999999329447746D;
        double d2 = mc.renderViewEntity.prevPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.prevPosZ) * (double)par1;
        int j = MathHelper.floor_double(d1 / 2048D);
        int k = MathHelper.floor_double(d2 / 2048D);
        d1 -= j * 2048;
        d2 -= k * 2048;
        float f8 = (worldObj.worldProvider.getCloudHeight() - f) + 0.33F;
        float f9 = (float)(d1 * (double)f5);
        float f10 = (float)(d2 * (double)f5);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, opaqueFlatClouds ? 1.0F : 0.8F);

        for (int l = -byte0 * i; l < byte0 * i; l += byte0)
        {
            for (int i1 = -byte0 * i; i1 < byte0 * i; i1 += byte0)
            {
                tessellator.addVertexWithUV(l + 0, f8, i1 + byte0, (float)(l + 0) * f5 + f9, (float)(i1 + byte0) * f5 + f10);
                tessellator.addVertexWithUV(l + byte0, f8, i1 + byte0, (float)(l + byte0) * f5 + f9, (float)(i1 + byte0) * f5 + f10);
                tessellator.addVertexWithUV(l + byte0, f8, i1 + 0, (float)(l + byte0) * f5 + f9, (float)(i1 + 0) * f5 + f10);
                tessellator.addVertexWithUV(l + 0, f8, i1 + 0, (float)(l + 0) * f5 + f9, (float)(i1 + 0) * f5 + f10);
            }
        }

        tessellator.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    private FloatBuffer a(float f1, float f2, float f3, float f4)
    {
        floatBuffer.clear();
        floatBuffer.put(f1).put(0.0F).put(f3).put(0.0F);
        floatBuffer.flip();
        return floatBuffer;
    }

    /**
     * Renders the 3d fancy clouds
     */
    public void renderCloudsFancy(float par1)
    {
        GL11.glDisable(GL11.GL_CULL_FACE);
        float f = (float)(mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY) * (double)par1);
        Tessellator tessellator = Tessellator.instance;
        float f1 = 12F;
        float f2 = 4F;
        double d = (float)cloudOffsetX + par1;
        double d1 = (mc.renderViewEntity.prevPosX + (mc.renderViewEntity.posX - mc.renderViewEntity.prevPosX) * (double)par1 + d * 0.029999999329447746D) / (double)f1;
        double d2 = (mc.renderViewEntity.prevPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.prevPosZ) * (double)par1) / (double)f1 + 0.33000001311302185D;
        float f3 = (worldObj.worldProvider.getCloudHeight() - f) + 0.33F;
        int i = MathHelper.floor_double(d1 / 2048D);
        int j = MathHelper.floor_double(d2 / 2048D);
        d1 -= i * 2048;
        d2 -= j * 2048;
        if (texClouds){
            OpenGlHelper.setActiveTexture(33985);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderEngine.getTexture("/olddays/fluff.png"));
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGen(8192, 9473, a(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGen(8193, 9473, a(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();
            GL11.glScalef(0.25F, 0.25F, 0.25F);
            GL11.glTranslatef((float)d1, (float)d2, 0.0F);
            GL11.glMatrixMode(5888);
            OpenGlHelper.setActiveTexture(33984);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderEngine.getTexture("/environment/clouds.png"));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Vec3 vec3d = worldObj.drawClouds(par1);
        float f4 = (float)vec3d.xCoord;
        float f5 = (float)vec3d.yCoord;
        float f6 = (float)vec3d.zCoord;

        if (mc.gameSettings.anaglyph)
        {
            float f7 = (f4 * 30F + f5 * 59F + f6 * 11F) / 100F;
            float f9 = (f4 * 30F + f5 * 70F) / 100F;
            float f11 = (f4 * 30F + f6 * 70F) / 100F;
            f4 = f7;
            f5 = f9;
            f6 = f11;
        }

        float f8 = (float)(d1 * 0.0D);
        float f10 = (float)(d2 * 0.0D);
        float f12 = 0.00390625F;
        f8 = (float)MathHelper.floor_double(d1) * f12;
        f10 = (float)MathHelper.floor_double(d2) * f12;
        float f13 = (float)(d1 - (double)MathHelper.floor_double(d1));
        float f14 = (float)(d2 - (double)MathHelper.floor_double(d2));
        int k = 8;
        byte byte0 = 4;
        float f15 = 0.0009765625F;
        GL11.glScalef(f1, 1.0F, f1);

        for (int l = 0; l < 2; l++)
        {
            if (l == 0)
            {
                GL11.glColorMask(false, false, false, false);
            }
            else if (mc.gameSettings.anaglyph)
            {
                if (EntityRenderer.anaglyphField == 0)
                {
                    GL11.glColorMask(false, true, true, true);
                }
                else
                {
                    GL11.glColorMask(true, false, false, true);
                }
            }
            else
            {
                GL11.glColorMask(true, true, true, true);
            }

            for (int i1 = -byte0 + 1; i1 <= byte0; i1++)
            {
                for (int j1 = -byte0 + 1; j1 <= byte0; j1++)
                {
                    tessellator.startDrawingQuads();
                    float f16 = i1 * k;
                    float f17 = j1 * k;
                    float f18 = f16 - f13;
                    float f19 = f17 - f14;

                    if (f3 > -f2 - 1.0F)
                    {
                        tessellator.setColorRGBA_F(f4 * 0.7F, f5 * 0.7F, f6 * 0.7F, 0.8F);
                        tessellator.setNormal(0.0F, -1F, 0.0F);
                        tessellator.addVertexWithUV(f18 + 0.0F, f3 + 0.0F, f19 + (float)k, (f16 + 0.0F) * f12 + f8, (f17 + (float)k) * f12 + f10);
                        tessellator.addVertexWithUV(f18 + (float)k, f3 + 0.0F, f19 + (float)k, (f16 + (float)k) * f12 + f8, (f17 + (float)k) * f12 + f10);
                        tessellator.addVertexWithUV(f18 + (float)k, f3 + 0.0F, f19 + 0.0F, (f16 + (float)k) * f12 + f8, (f17 + 0.0F) * f12 + f10);
                        tessellator.addVertexWithUV(f18 + 0.0F, f3 + 0.0F, f19 + 0.0F, (f16 + 0.0F) * f12 + f8, (f17 + 0.0F) * f12 + f10);
                    }

                    if (f3 <= f2 + 1.0F)
                    {
                        tessellator.setColorRGBA_F(f4, f5, f6, 0.8F);
                        tessellator.setNormal(0.0F, 1.0F, 0.0F);
                        tessellator.addVertexWithUV(f18 + 0.0F, (f3 + f2) - f15, f19 + (float)k, (f16 + 0.0F) * f12 + f8, (f17 + (float)k) * f12 + f10);
                        tessellator.addVertexWithUV(f18 + (float)k, (f3 + f2) - f15, f19 + (float)k, (f16 + (float)k) * f12 + f8, (f17 + (float)k) * f12 + f10);
                        tessellator.addVertexWithUV(f18 + (float)k, (f3 + f2) - f15, f19 + 0.0F, (f16 + (float)k) * f12 + f8, (f17 + 0.0F) * f12 + f10);
                        tessellator.addVertexWithUV(f18 + 0.0F, (f3 + f2) - f15, f19 + 0.0F, (f16 + 0.0F) * f12 + f8, (f17 + 0.0F) * f12 + f10);
                    }

                    tessellator.setColorRGBA_F(f4 * 0.9F, f5 * 0.9F, f6 * 0.9F, 0.8F);

                    if (i1 > -1)
                    {
                        tessellator.setNormal(-1F, 0.0F, 0.0F);

                        for (int k1 = 0; k1 < k; k1++)
                        {
                            tessellator.addVertexWithUV(f18 + (float)k1 + 0.0F, f3 + 0.0F, f19 + (float)k, (f16 + (float)k1 + 0.5F) * f12 + f8, (f17 + (float)k) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + (float)k1 + 0.0F, f3 + f2, f19 + (float)k, (f16 + (float)k1 + 0.5F) * f12 + f8, (f17 + (float)k) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + (float)k1 + 0.0F, f3 + f2, f19 + 0.0F, (f16 + (float)k1 + 0.5F) * f12 + f8, (f17 + 0.0F) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + (float)k1 + 0.0F, f3 + 0.0F, f19 + 0.0F, (f16 + (float)k1 + 0.5F) * f12 + f8, (f17 + 0.0F) * f12 + f10);
                        }
                    }

                    if (i1 <= 1)
                    {
                        tessellator.setNormal(1.0F, 0.0F, 0.0F);

                        for (int l1 = 0; l1 < k; l1++)
                        {
                            tessellator.addVertexWithUV((f18 + (float)l1 + 1.0F) - f15, f3 + 0.0F, f19 + (float)k, (f16 + (float)l1 + 0.5F) * f12 + f8, (f17 + (float)k) * f12 + f10);
                            tessellator.addVertexWithUV((f18 + (float)l1 + 1.0F) - f15, f3 + f2, f19 + (float)k, (f16 + (float)l1 + 0.5F) * f12 + f8, (f17 + (float)k) * f12 + f10);
                            tessellator.addVertexWithUV((f18 + (float)l1 + 1.0F) - f15, f3 + f2, f19 + 0.0F, (f16 + (float)l1 + 0.5F) * f12 + f8, (f17 + 0.0F) * f12 + f10);
                            tessellator.addVertexWithUV((f18 + (float)l1 + 1.0F) - f15, f3 + 0.0F, f19 + 0.0F, (f16 + (float)l1 + 0.5F) * f12 + f8, (f17 + 0.0F) * f12 + f10);
                        }
                    }

                    tessellator.setColorRGBA_F(f4 * 0.8F, f5 * 0.8F, f6 * 0.8F, 0.8F);

                    if (j1 > -1)
                    {
                        tessellator.setNormal(0.0F, 0.0F, -1F);

                        for (int i2 = 0; i2 < k; i2++)
                        {
                            tessellator.addVertexWithUV(f18 + 0.0F, f3 + f2, f19 + (float)i2 + 0.0F, (f16 + 0.0F) * f12 + f8, (f17 + (float)i2 + 0.5F) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + (float)k, f3 + f2, f19 + (float)i2 + 0.0F, (f16 + (float)k) * f12 + f8, (f17 + (float)i2 + 0.5F) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + (float)k, f3 + 0.0F, f19 + (float)i2 + 0.0F, (f16 + (float)k) * f12 + f8, (f17 + (float)i2 + 0.5F) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + 0.0F, f3 + 0.0F, f19 + (float)i2 + 0.0F, (f16 + 0.0F) * f12 + f8, (f17 + (float)i2 + 0.5F) * f12 + f10);
                        }
                    }

                    if (j1 <= 1)
                    {
                        tessellator.setNormal(0.0F, 0.0F, 1.0F);

                        for (int j2 = 0; j2 < k; j2++)
                        {
                            tessellator.addVertexWithUV(f18 + 0.0F, f3 + f2, (f19 + (float)j2 + 1.0F) - f15, (f16 + 0.0F) * f12 + f8, (f17 + (float)j2 + 0.5F) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + (float)k, f3 + f2, (f19 + (float)j2 + 1.0F) - f15, (f16 + (float)k) * f12 + f8, (f17 + (float)j2 + 0.5F) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + (float)k, f3 + 0.0F, (f19 + (float)j2 + 1.0F) - f15, (f16 + (float)k) * f12 + f8, (f17 + (float)j2 + 0.5F) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + 0.0F, f3 + 0.0F, (f19 + (float)j2 + 1.0F) - f15, (f16 + 0.0F) * f12 + f8, (f17 + (float)j2 + 0.5F) * f12 + f10);
                        }
                    }

                    tessellator.draw();
                }
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
        if (texClouds){
            OpenGlHelper.setActiveTexture(33985);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(3168);
            GL11.glDisable(3169);
            OpenGlHelper.setActiveTexture(33984);
        }
    }
}