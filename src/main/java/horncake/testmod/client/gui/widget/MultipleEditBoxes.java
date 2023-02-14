package horncake.testmod.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import java.util.*;
import java.util.List;

public class MultipleEditBoxes {
    private final List<EditBox> boxList;
    private final Map<String, Integer> boxMap;
    private final List<String> keyList;

    private final int size;

    public MultipleEditBoxes(int size, String ... key){
        this(size, Arrays.stream(key).toList());
    }

    public MultipleEditBoxes(int size, List<String> key) {
        this.size = size;
        this.keyList = key;
        this.boxList = new ArrayList<>();
        this.boxMap = new HashMap<>(size);
        for(int i = 0; i < size; i++) {
            this.boxMap.put(key.get(i),i);
            this.boxList.add(i,new EditBox(Minecraft.getInstance().font, 0,0,0,0, Component.empty()));
        }
    }

    public int getIndex(String key) {
        return this.boxMap.get(key);
    }


    public void setEditBox(String key, EditBox box) {
        this.boxList.set(getIndex(key),box);
    }


    public void setValueFromTag(String key, CompoundTag tag) {
        this.getBox(key).setValue(String.valueOf(tag.getInt(key)));
    }

    public void setValue(String key, String text) {
        this.getBox(key).setValue(text);
    }

    public void setAllValue(String text) {
        for(String s : keyList) {
            setValue(s,text);
        }
    }

    public String getValue(String key) {
        return this.getBox(key).getValue();
    }

    public List<String> getAllValue() {
        return new ArrayList<>() {
            {
                for (String s : keyList) {
                    getValue(s);
                }
            }
        };
    }

    public List<EditBox> getBoxList() {
        return this.boxList;
    }

    public EditBox getBox(String key) {
        return this.boxList.get(getIndex(key));
    }

    public void resetFocus() {
        keyList.forEach(key -> this.getBox(key).setFocus(false));
    }

    public void setFocus(String key) {
        this.resetFocus();
        this.getBox(key).setFocus(true);
        this.getBox(key).active = true;
    }

    public String getFocus() {
        for (String s : keyList) {
            if(getBox(s).isFocused()) return s;
        }
        return null;
    }

    public void render(String key, PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.getBox(key).render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    public void tick() {
        for(String s : keyList) {
            this.getBox(s).tick();
        }
    }

    public List<String> getKeyList() {
        return keyList;
    }
}
