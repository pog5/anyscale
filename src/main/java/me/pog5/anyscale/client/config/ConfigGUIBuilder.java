package me.pog5.anyscale.client.config;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.pog5.anyscale.client.AnyscaleClient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ConfigGUIBuilder {
    private static final AnyscaleConfigStore store = new AnyscaleConfigStore();
    public static OptionPage Anyscale$buildOptionsPage() {
        List<OptionGroup> groups = new ArrayList<>();
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(int.class, store)
                        .setName(Text.translatable("Base (Menu) Scale"))
                        .setTooltip(Text.translatable("This value is the base GUI scale, the same from General, except more detailed. All other values here are based on top of it. It gets divided by 100 (350 -> 3.5). Requires you to close the current menu and open it again to take effect."))
                        .setControl(option -> new SliderControl(option, 50, 500, 5, x->Text.literal(String.valueOf((float) x/100)))) // ==32?"Vanilla":(x==256?"Keep All":x+" chunks")
                        .setImpact(OptionImpact.LOW)
                        .setEnabled(AnyscaleClient.IS_ENABLEED)
                        .setBinding((opts, value) -> {
                            opts.base_scale = (float) value / 100;
                        }, opts -> (int) (opts.base_scale * 100))
                        .setFlags()
                        .build()
                )
                .add(OptionImpl.createBuilder(int.class, store)
                        .setName(Text.translatable("Inventory Scale"))
                        .setTooltip(Text.translatable("This value controls the size of (Ender) Chests, Barrels, Hoppers, Inventories, etc... It gets divided by 100 (350 -> 3.5). Requires you to close the current menu and open it again to take effect."))
                        .setControl(option -> new SliderControl(option, 50, 500, 5, x->Text.literal(String.valueOf((float) x/100)))) // ==32?"Vanilla":(x==256?"Keep All":x+" chunks")
                        .setImpact(OptionImpact.LOW)
                        .setEnabled(AnyscaleClient.IS_ENABLEED)
                        .setBinding((opts, value) -> {
                            opts.menu_scale = (float) value / 100;
                        }, opts -> (int) (opts.menu_scale * 100))
                        .setFlags()
                        .build()
                )
                .add(OptionImpl.createBuilder(int.class, store)
                        .setName(Text.translatable("Player List (TAB) Scale"))
                        .setTooltip(Text.translatable("Player List (TAB) scaling, gets divided by 100 (350 -> 3.5)"))
                        .setControl(option -> new SliderControl(option, 0, 500, 5, x->Text.literal( x==0 ? "Disabled" : String.valueOf((float) x/100)))) // ==32?"Vanilla":(x==256?"Keep All":x+" chunks")
                        .setImpact(OptionImpact.LOW)
                        .setEnabled(AnyscaleClient.IS_ENABLEED)
                        .setBinding((opts, value) -> opts.playerlist_scale = (float) value / 100, opts -> (int) (opts.playerlist_scale * 100))
                        .setFlags()
                        .build()
                )
                .add(OptionImpl.createBuilder(int.class, store)
                        .setName(Text.translatable("Scoreboard Scale"))
                        .setTooltip(Text.translatable("Sidebar scoreboard scaling, gets divided by 100 (350 -> 3.5)"))
                        .setControl(option -> new SliderControl(option, 0, 500, 5, x->Text.literal( x==0 ? "Disabled" : String.valueOf((float) x/100)))) // ==32?"Vanilla":(x==256?"Keep All":x+" chunks")
                        .setImpact(OptionImpact.LOW)
                        .setEnabled(AnyscaleClient.IS_ENABLEED)
                        .setBinding((opts, value) -> opts.scoreboard_scale = (float) value / 100, opts -> (int) (opts.scoreboard_scale * 100))
                        .setFlags()
                        .build()
                )
                .add(OptionImpl.createBuilder(int.class, store)
                        .setName(Text.translatable("Chat Scale"))
                        .setTooltip(Text.translatable("Chatbox and Chat messages scaling, gets divided by 100 (350 -> 3.5)"))
                        .setControl(option -> new SliderControl(option, 0, 500, 5, x->Text.literal( x==0 ? "Disabled" : String.valueOf((float) x/100)))) // ==32?"Vanilla":(x==256?"Keep All":x+" chunks")
                        .setImpact(OptionImpact.LOW)
                        .setEnabled(AnyscaleClient.IS_ENABLEED)
                        .setBinding((opts, value) -> opts.chat_scale = (float) value / 100, opts -> (int) (opts.chat_scale * 100))
                        .setFlags()
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, store)
                        .setName(Text.translatable("Opening a inventory disables chat"))
                        .setTooltip(Text.translatable("Disable chat when a inventory opens"))
                        .setControl(TickBoxControl::new) // ==32?"Vanilla":(x==256?"Keep All":x+" chunks")
                        .setImpact(OptionImpact.LOW)
                        .setEnabled(AnyscaleClient.IS_ENABLEED)
                        .setBinding((opts, value) -> opts.inventory_disables_chat = value, opts -> opts.inventory_disables_chat)
                        .setFlags()
                        .build()
                )
                .add(OptionImpl.createBuilder(int.class, store)
                        .setName(Text.translatable("Hotbar Scale"))
                        .setTooltip(Text.translatable("Hotbar scaling, gets divided by 100 (350 -> 3.5)"))
                        .setControl(option -> new SliderControl(option, 0, 500, 5, x->Text.literal( x==0 ? "Disabled" : String.valueOf((float) x/100)))) // ==32?"Vanilla":(x==256?"Keep All":x+" chunks")
                        .setImpact(OptionImpact.LOW)
                        .setEnabled(AnyscaleClient.IS_ENABLEED)
                        .setBinding((opts, value) -> opts.hotbar_scale = (float) value / 100, opts -> (int) (opts.hotbar_scale * 100))
                        .setFlags()
                        .build()
                )

                .build());

        return new OptionPage(Text.of("UI Scaling"), ImmutableList.copyOf(groups));

    }
}
