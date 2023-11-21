package kr.cosine.parkour.config

import kr.cosine.parkour.announce.ParkourAnnounce
import kr.cosine.parkour.announce.ParkourChat
import kr.cosine.parkour.announce.ParkourSound
import kr.cosine.parkour.announce.ParkourTitle
import kr.cosine.parkour.data.Message
import kr.cosine.parkour.enums.Announce
import kr.cosine.parkour.enums.MessageType
import kr.cosine.parkour.registry.AnnounceRegistry
import kr.cosine.parkour.registry.MessageRegistry
import kr.cosine.parkour.registry.PlaceholderRegistry
import kr.cosine.parkour.registry.SettingRegistry
import kr.cosine.parkour.registry.SettingRegistry.Companion.prefix
import kr.hqservice.framework.bukkit.core.extension.colorize
import kr.hqservice.framework.global.core.component.Bean
import kr.hqservice.framework.yaml.config.HQYamlConfiguration
import java.util.logging.Logger

@Bean
class SettingConfig(
    private val logger: Logger,
    private val config: HQYamlConfiguration,
    private val settingRegistry: SettingRegistry,
    private val placeholderRegistry: PlaceholderRegistry,
    private val messageRegistry: MessageRegistry,
    private val announceRegistry: AnnounceRegistry
) {

    fun load() {
        loadSettingSection()
        loadPlaceholderSection()
        loadMessageSection()
        loadAnnounceSection()
    }

    private fun loadSettingSection() {
        config.getSection("setting")?.apply {
            prefix = getString("prefix").colorize()
            val useGiftBox = getBoolean("use-giftbox")
            val preventedCommand = getBoolean("prevented-command")
            settingRegistry.apply {
                setUseGiftBox(useGiftBox)
                setPreventedCommand(preventedCommand)
            }
        }
    }

    private fun loadPlaceholderSection() {
        config.getSection("placeholder")?.apply {
            val recordedNoneMessage = getString("recorded-none").colorize()
            val rankingMessage = getString("ranking").colorize()
            placeholderRegistry.apply {
                setRecordedNoneMessage(recordedNoneMessage)
                setRankingMessage(rankingMessage)
            }
        }
    }

    private fun loadMessageSection() {
        val messageSectionKey = "message"
        config.getSection(messageSectionKey)?.apply {
            getKeys().forEach { errorText ->
                val error = MessageType.findMessageType(errorText) ?: run {
                    logger.warning("$messageSectionKey 섹션에 ${errorText}은(는) 존재하지 않는 Error입니다.")
                    return@forEach
                }
                val message = getString(errorText).applyPrefix()
                val errorMessage = Message(message)
                messageRegistry.setMessage(error, errorMessage)
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
        messageRegistry.clear()
        announceRegistry.clear()
        load()
    }
}