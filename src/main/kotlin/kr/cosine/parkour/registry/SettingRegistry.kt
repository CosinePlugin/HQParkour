package kr.cosine.parkour.registry

import kr.hqservice.framework.bukkit.core.extension.colorize
import kr.hqservice.framework.global.core.component.Bean

@Bean
class SettingRegistry {

    internal companion object {
        var prefix = "<g:c3eb34>§l[Parkour]</g:0ae9f5>§f".colorize()
    }

    var useGiftBox = false
        private set

    var preventedCommand = false
        private set

    fun setUseGiftBox(useGiftBox: Boolean) {
        this.useGiftBox = useGiftBox
    }

    fun setPreventedCommand(preventedCommand: Boolean) {
        this.preventedCommand = preventedCommand
    }
}