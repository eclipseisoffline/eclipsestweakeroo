package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import java.util.Map;
import java.util.UUID;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BossBarHud.class)
public interface BossBarHudAccessor {

    @Accessor
    Map<UUID, ClientBossBar> getBossBars();
}
