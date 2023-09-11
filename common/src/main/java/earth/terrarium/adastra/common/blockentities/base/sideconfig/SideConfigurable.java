package earth.terrarium.adastra.common.blockentities.base.sideconfig;

import java.util.List;

public interface SideConfigurable {

    List<ConfigurationEntry> getSideConfig();

    List<ConfigurationEntry> defaultConfig();

    default void resetToDefault(int index) {
        var entries = this.getSideConfig();
        entries.get(index).sides().clear();
        entries.get(index).sides().putAll(this.defaultConfig().get(index).sides());
    }
}
