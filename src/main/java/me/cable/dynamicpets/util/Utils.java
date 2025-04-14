package me.cable.dynamicpets.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public final class Utils {

    /*
        Gradient example: <gradient:#FF0000:#0000FF>Hello world!</gradient>
     */
    public static @NotNull Component miniMessage(@Nullable String message) {
        // the <!i> removes default italics
        return (message == null) ? Component.empty() : MiniMessage.miniMessage().deserialize("<!i>" + message).asComponent();
    }

    public static @NotNull List<Component> miniMessage(@NotNull List<String> lines) {
        return lines.stream().map(Utils::miniMessage).toList();
    }

    public static void insertListToList(@NotNull List<String> list, @NotNull String placeholder, @NotNull List<String> insert) {
        for (int i = 0; i < list.size(); i++) {
            String line = list.get(i);
            if (!line.contains(placeholder)) continue;

            String[] parts;

            if (line.equals(placeholder)) {
                parts = new String[]{"", ""}; // nothing to add on either side
            } else {
                parts = line.split(Pattern.quote(placeholder));
            }

            list.remove(i);

            for (String s : insert) {
                list.add(i++, parts[0] + s + parts[1]);
            }

            i--;
        }
    }

    public static @NotNull ItemStack createHead(@NotNull String base64Texture) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        head.editMeta(SkullMeta.class, skullMeta -> {
            PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID());
            playerProfile.setProperty(new ProfileProperty("textures", base64Texture));
            skullMeta.setPlayerProfile(playerProfile);
        });

        return head;
    }
}
