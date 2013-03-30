package net.minecraft.src;

import net.minecraft.client.Minecraft;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OldDaysModule{
    public int id;
    public String name;
    public ArrayList<OldDaysProperty> properties;
    protected mod_OldDays core;
    protected Minecraft minecraft;
    public int last;
    public boolean renderersAdded;
    public boolean isLocal;

    public OldDaysModule(mod_OldDays c, int i, String s){
        core = c;
        id = i;
        name = s;
        properties = new ArrayList<OldDaysProperty>();
        minecraft = mod_OldDays.getMinecraft();
        last = 0;
        renderersAdded = false;
        isLocal = true;
    }

    public OldDaysProperty getPropertyById(int id){
        for (int i = 0; i < properties.size(); i++){
            OldDaysProperty prop = properties.get(i);
            if (prop.id == id){
                return prop;
            }
        }
        return null;
    }

    public void addSound(int id, String name){
        File sound = new File(mod_OldDays.getMinecraft().mcDataDir, "resources/sound3/olddays/"+name+".ogg");
        core.unpackSound("sound3/olddays", name+".ogg");
        if (sound.exists()){
            mod_OldDays.getMinecraft().installResource("sound3/olddays/"+sound.getName(), sound);
        }else{
            getPropertyById(id).noSounds = true;
        }
    }

    public void addMusic(int id, String name){
        File sound = new File(mod_OldDays.getMinecraft().mcDataDir, "resources/music/"+name+".ogg");
        core.unpackSound("music", name+".ogg");
        if (sound.exists()){
            mod_OldDays.getMinecraft().installResource("music/"+sound.getName(), sound);
        }else{
            getPropertyById(id).noSounds = true;
        }
    }

    public void addRecipe(ItemStack stack, Object... obj){
        CraftingManager.getInstance().addRecipe(stack, obj);
    }

    public void addShapelessRecipe(ItemStack stack, Object... obj){
        CraftingManager.getInstance().addShapelessRecipe(stack, obj);
    }

    public void dumpRecipes(String str){
        try{
            List list = CraftingManager.getInstance().getRecipeList();
            for (int i = 0; i < list.size(); i++){
                ItemStack stack = ((IRecipe)list.get(i)).getRecipeOutput();
                if (stack == null){
                    continue;
                }
                String match = stack.toString();
                if (str == null || match.contains(str)){
                    System.out.println("OldDays: Found recipe: "+match);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void removeRecipe(String str1){
        try{
            int count = 0;
            List list = CraftingManager.getInstance().getRecipeList();
            for (int i = 0; i < list.size(); i++){
                ItemStack stack = ((IRecipe)list.get(i)).getRecipeOutput();
                if (stack == null){
                    continue;
                }
                String match = stack.toString();
//                 System.out.println(match);
                if (match.equals(str1)){
                    list.remove(i);
                    count++;
                }
            }
            if (count <= 0){
//                 System.out.println("OldDays: Invalid recipe identifier: "+str1);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void reload(){
        try{
            mod_OldDays.getMinecraft().renderGlobal.updateAllRenderers(true);
        }catch(Throwable t){
            try{
                mod_OldDays.getMinecraft().renderGlobal.loadRenderers();
            }catch(Exception e){}
        }
    }

    public static void addTextureHook(String origname, int origi, String newname, int newi){
        mod_OldDays.texman.addTextureHook(origname, origi, newname, newi);
    }

    public static void addTextureHook(String origname, int origi, String newname, int newi, int w, int h){
        mod_OldDays.texman.addTextureHook(origname, origi, newname, newi, w, h);
    }

    public static void setTextureHook(String origname, String newname, boolean b){
        mod_OldDays.texman.setTextureHook(origname, newname, b);
    }

    public static void setTextureHook(String name, int i2, String name2, int index, boolean b){
        mod_OldDays.texman.setTextureHook(name, i2, name2, index, b);
    }

    public static boolean hasTextures(String... str){
        return mod_OldDays.texman.hasEntry(str);
    }

    public static boolean hasIcons(boolean b, String... str){
        return mod_OldDays.texman.hasIcons(b, str);
    }

    public void callback(int i){}

    public void set(Class c, String name, Object value, boolean necessary){
        if (c == net.minecraft.src.EntityPlayer.class){
            try{
                (net.minecraft.src.EntityPlayerSP2.class).getDeclaredField(name).set(null, value);
            }catch(Exception e){}
        }
        try{
            c.getDeclaredField(name).set(null, value);
        }catch(Exception ex){
            if (necessary){
                System.out.println("No \""+name+"\" field at the "+c.getName()+" class");
                if (getPropertyById(last) != null){
                    getPropertyById(last).disable();
                }
            }
        }
    }

    public void set(Class c, String name, Object value){
        set(c, name, value, true);
    }

    public void addRenderer(Class c, Render r){
        RenderManager renderMan = RenderManager.instance;
        try{
            r.setRenderManager(renderMan);
            HashMap map = ((HashMap)mod_OldDays.getField(RenderManager.class, renderMan, 0));
            if (map.get(c) != null && map.get(c).getClass() == r.getClass()){
                return;
            }
            map.put(c, r);
            renderersAdded = true;
            System.out.println("OldDays: Added "+r.getClass().getName()+" renderer");
        }catch(Exception ex){
            System.out.println("OldDays: Failed to add renderer: "+ex);
            ex.printStackTrace();
        }
    }

    public void addEntity(Class par0Class, String par1Str, int par2){
        addEntity(par0Class, par1Str, par2, -1, -1);
    }

    public void addEntity(Class par0Class, String par1Str, int par2, int par3, int par4){
        try{
            int id = 1;
            Method m = null;
            Method[] methods = (EntityList.class).getDeclaredMethods();
            for (int i = 0; i < methods.length; i++){
                if (par3 >= 0 && par4 >= 0 && methods[i].toGenericString().matches("^private static void (net.minecraft.src.)?([a-zA-Z]{1,10}).[a-zA-Z]{1,10}.java.lang.Class,java.lang.String,int,int,int.$")){
                    m = methods[i];
                    break;
                }
                if (methods[i].toGenericString().matches("^private static void (net.minecraft.src.)?([a-zA-Z]{1,10}).[a-zA-Z]{1,10}.java.lang.Class,java.lang.String,int.$")){
                    m = methods[i];
                    break;
                }
            }
            m.setAccessible(true);
            if (par3 >= 0 && par4 >= 0){
                m.invoke(null, par0Class, par1Str, par2, par3, par4);
            }else{
                m.invoke(null, par0Class, par1Str, par2);
            }
            System.out.println("OldDays: Added "+par1Str+" entity");
        }catch(Exception ex){
            System.out.println("OldDays: Failed to add entity: "+ex);
        }
    }

    public static void setInWorldInfo(String var, Object b){
        World world = mod_OldDays.getMinecraft().theWorld;
        if (world==null){
            return;
        }
        WorldInfo info = world.getWorldInfo();
        try{
            info.getClass().getDeclaredField(var).set(info, b);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public boolean renderBlocks(RenderBlocks r, IBlockAccess i, Block b, int x, int y, int z, int id, Icon override){
        return false;
    }

    public void registerKey(KeyBinding key){
        core.registerKey(key);
    }

    public void catchKeyEvent(KeyBinding keybinding){}

    public boolean onTick(){
        return true;
    }

    public boolean onGUITick(GuiScreen gui){
        return true;
    }

    public void onLoadingSP(String par1Str, String par2Str){}

    public void onFallbackChange(boolean fallback){}

    public String[] getAdditionalPackageData(){
        return null;
    }

    public void readAdditionalPackageData(String[] data){}

    public void refreshTextures(){}
}