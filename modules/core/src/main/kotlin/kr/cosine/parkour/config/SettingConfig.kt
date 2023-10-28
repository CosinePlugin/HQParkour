package kr.cosine.parkour.config

import kr.cosine.parkour.announce.ParkourAnnounce
import kr.cosine.parkour.announce.ParkourChat
import kr.cosine.parkour.announce.ParkourSound
import kr.cosine.parkour.announce.ParkourTitle
import kr.cosine.parkour.data.ErrorMessage
import kr.cosine.parkour.enums.Announce
import kr.cosine.parkour.enums.Error
import kr.cosine.parkour.registry.AnnounceRegistry
import kr.cosine.parkour.registry.ErrorMessageRegistry
import kr.hqservice.framework.bukkit.core.extension.colorize
import kr.hqservice.framework.global.core.component.Bean
import kr.hqservice.framework.yaml.config.HQYamlConfiguration
import java.util.logging.Logger

@Bean
class SettingConfig(
    private val logger: Logger,
    private val config: HQYamlConfiguration,
    private val errorMessageRegistry: ErrorMessageRegistry,
    private val announceRegistry: AnnounceRegistry
) {

    internal companion object {
        var prefix = "<g:c3eb34>§l[Parkour]</g:0ae9f5>§f".colorize()
            private set
    }

    var useGiftBox = false
        private set

    fun load() {
        loadSettingSection()
        loadMessageSection()
        loadAnnounceSection()
    }

    private fun loadSettingSection() {
        config.getSection("setting")?.apply {
            prefix = config.getString("prefix").colorize()
            useGiftBox = config.getBoolean("use-giftbox")
        }
    }

    private fun loadMessageSection() {
        val messageSectionKey = "message"
        config.getSection(messageSectionKey)?.apply {
            getKeys().forEach { errorText ->
                val error = Error.getError(errorText) ?: run {
                    logger.warning("$messageSectionKey 섹션에 ${errorText}은(는) 존재하지 않는 Error입니다.")
                    return@forEach
                }
                val message = getString(errorText).applyPrefix()
                val errorMessage = ErrorMessage(message)
                errorMessageRegistry.setErrorMessage(error, errorMessage)
            }
        }
    }

    private fun loadAnnounceSection() {
        val announceSectionKey = "announce"
        config.getSection(announceSectionKey)?.apply {
            getKeys().forEach { announceText ->
                val announce = Announce.getAnnounce(announceText) ?: run {
                    logger.warning("$announceSectionKey 섹션에 ${announceText}은(는) 존재하지 않는 Announce입니다.")
                    return@forEach
                }
                getSection(announceText)?.apply {
                    val soundSectionKey = "sound"
                    val soundSection = getSection(soundSectionKey) ?: run {
                        logger.warning("$announceSectionKey.$announceText 섹션에 $soundSectionKey 섹션이 존재하지 않습니다.")
                        return@forEach
                    }
                    val chatSectionKey = "chat"
                    val chatSection = getSection(chatSectionKey) ?: run {
                        logger.warning("$announceSectionKey.$announceText 섹션에 $chatSectionKey 섹션이 존재하지 않습니다.")
                        return@forEach
                    }
                    val titleSectionKey = "title"
                    val titleSection = getSection(titleSectionKey) ?: run {
                        logger.warning("$announceSectionKey.$announceText 섹션에 $titleSectionKey 섹션이 존재하지 않습니다.")
                        return@forEach
                    }
                    val parkourSound = soundSection.let {
                        val isEnabled = it.getBoolean("enabled")
                        val sound = it.getString("name")
                        val volume = it.getDouble("volume").toFloat()
                        val pitch = it.getDouble("pitch").toFloat()
                        ParkourSound(isEnabled, sound, volume, pitch)
                    }
                    val parkourChat = chatSection.let {
                        val isEnabled = it.getBoolean("enabled")
                        val isBroadcast = it.getBoolean("broadcast")
                        val message = it.getString("message").applyPrefix()
                        ParkourChat(isEnabled, isBroadcast, message)
                    }
                    val parkourTitle = titleSection.let {
                        val isEnabled = it.getBoolean("enabled")
                        val title = it.getString("title").applyPrefix()
                        val subTitle = it.getString("sub-title").applyPrefix()
                        val fadeIn = it.getInt("fade-in")
                        val duration = it.getInt("duration")
                        val fadeOut = it.getInt("fade-out")
                        ParkourTitle(isEnabled, title, subTitle, fadeIn, duration, fadeOut)
                    }
                    val parkourAnnounce = ParkourAnnounce(parkourSound, parkourChat, parkourTitle)
                    announceRegistry.setParkourAnnounce(announce, parkourAnnounce)
                }
            }
        }
    }

    private fun String.applyPrefix(): String {
        return colorize().replace("%prefix%", prefix)
    }

    fun reload() {
        config.reload()
        errorMessageRegistry.clear()
        announceRegistry.clear()
        load()
    }
}