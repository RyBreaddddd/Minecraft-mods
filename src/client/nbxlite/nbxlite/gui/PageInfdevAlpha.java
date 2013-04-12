package net.minecraft.src.nbxlite.gui;

import java.util.Random;
import net.minecraft.src.*;

public class PageInfdevAlpha extends Page{
    private GuiButton alphaThemeButton;
    private GuiButton newOresButton;
    private boolean newores;
    private int mode;

    public PageInfdevAlpha(GuiNBXlite parent, int mode){
        super(parent);
        newores = ODNBXlite.DefaultNewOres;
        this.mode = mode;
    }

    @Override
    public void initButtons(){
        buttonList.add(alphaThemeButton = new GuiButton(0, width / 2 - 75 + leftmargin, height / 6 + 44, 150, 20, ""));
        buttonList.add(newOresButton = new GuiButton(1, width / 2 - 75 + leftmargin, height / 6 + 84, 150, 20, ""));
    }

    @Override
    public void updateButtonText(){
        StringTranslate stringtranslate = StringTranslate.getInstance();
        newOresButton.displayString = mod_OldDays.lang.get("nbxlite.generatenewores.name") + ": " + stringtranslate.translateKey("options." + (newores ? "on" : "off"));
        alphaThemeButton.displayString = mod_OldDays.lang.get("nbxlite.maptheme.name") + ": " + mod_OldDays.lang.get(GeneratorList.themename[GeneratorList.themecurrent]);
    }

    @Override
    public void updateButtonVisibility(){
        newOresButton.drawButton = mode < 2;
    }

    @Override
    public void drawScreen(int i, int j, float f){
        drawCenteredString(fontRenderer, mod_OldDays.lang.get(GeneratorList.themedesc[GeneratorList.themecurrent]), width / 2 + leftmargin, height / 6 + 67, 0xa0a0a0);
        super.drawScreen(i, j, f);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton){
        if (!guibutton.enabled){
            return;
        }
        if (guibutton == newOresButton){
            newores = !newores;
        }else if (guibutton == alphaThemeButton){
            if (GeneratorList.themecurrent < GeneratorList.themelength){
                GeneratorList.themecurrent++;
            }else{
                GeneratorList.themecurrent = 0;
            }
        }
        updateButtonPosition();
        updateButtonVisibility();
        updateButtonText();
    }

    @Override
    public void selectSettings(){
        ODNBXlite.Generator = ODNBXlite.GEN_BIOMELESS;
        ODNBXlite.MapFeatures = mode;
        ODNBXlite.MapTheme = GeneratorList.themecurrent;
        if(mode == 1 && (ODNBXlite.MapTheme == ODNBXlite.THEME_NORMAL || ODNBXlite.MapTheme == ODNBXlite.THEME_WOODS)){
            ODNBXlite.SnowCovered = (new Random()).nextInt(ODNBXlite.MapTheme == ODNBXlite.THEME_WOODS ? 2 : 4) == 0;
        }
        ODNBXlite.GenerateNewOres=newores;
    }
}