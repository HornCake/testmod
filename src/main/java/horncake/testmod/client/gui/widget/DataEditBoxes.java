package horncake.testmod.client.gui.widget;

import horncake.testmod.init.RegisterMessage;
import horncake.testmod.network.PacketMagicTableSetText;
import horncake.testmod.util.MediumData;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class DataEditBoxes extends MultipleEditBoxes{
    private static final int SIZE = MediumData.NAME_LIST2.size();
    public DataEditBoxes(int size, List<String> key) {
        super(size, key);
    }
    public DataEditBoxes(List<String> key) {
        super(SIZE,key);/*
        key.forEach(key1 -> {
            this.getBox(key1).setResponder(s -> {
                if(s != null) {
                    RegisterMessage.sendToServer(new PacketMagicTableSetText(s, key1));
                }
            });
        });
        */
    }

    public void setResponders() {
        List<String> keys = getKeyList();
        keys.forEach(key -> {
            this.getBox(key).setResponder(s -> {
                if(s != null) {
                    RegisterMessage.sendToServer(new PacketMagicTableSetText(s, key));
                }
            });
        });
    }

    @Override
    public void setValueFromTag(String key, CompoundTag tag) {
        if(key.matches(MediumData.R) || key.matches(MediumData.G) || key.matches(MediumData.B)){
            CompoundTag tag2 = tag.getCompound(MediumData.COLOR);
            super.setValueFromTag(key,tag2);
        } else {
            super.setValueFromTag(key, tag);
        }
    }
}
