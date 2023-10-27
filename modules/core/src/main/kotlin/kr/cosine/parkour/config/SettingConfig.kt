package kr.cosine.parkour.config

import kr.cosine.parkour.registry.AnnounceRegistry
import kr.cosine.parkour.registry.ErrorMessageRegistry
import kr.hqservice.framework.global.core.component.Bean
import kr.hqservice.framework.yaml.config.HQYamlConfiguration

@Bean
class SettingConfig(
    private val config: HQYamlConfiguration,
    private val errorMessageRegistry: ErrorMessageRegistry,
    private val announceRegistry: AnnounceRegistry
) {

    fun load() {

    }

    fun reload() {
        config.reload()
        load()
    }
}