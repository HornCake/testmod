package horncake.testmod.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.nbt.CompoundTag;

import java.util.*;

public class DataEditBoxes {
    private List<EditBox> boxList;
    private Map<String, Integer> boxMap;
    private List<String> keyList;

    private int size;

    public DataEditBoxes(int size, String ... key){
        this(size, Arrays.stream(key).toList());
    }

    public DataEditBoxes(int size, List<String> key) {
        this.size = size;
        this.keyList = key;
        this.boxList = new ArrayList<>(size);
        this.boxMap = new HashMap<>(size);
        for(int i = 0; i < size; i++) {
            this.boxMap.put(key.get(i),i);
        }
    }

    private int getIndex(String key) {
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

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        for(String s : keyList) {
            this.getBox(s).render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }
    }

    public void tick() {
        for(String s : keyList) {
            this.getBox(s).tick();
        }
    }
}
