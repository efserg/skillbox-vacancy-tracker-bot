package com.skillbox.vacancy.tracker.bot.command;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import com.skillbox.vacancy.tracker.bot.BotCommandStorage;
import com.skillbox.vacancy.tracker.bot.TelegramUpdateInfo;
import com.skillbox.vacancy.tracker.model.BotUser;
import com.skillbox.vacancy.tracker.service.UserService;

@RequiredArgsConstructor
public class SetTimeZoneCommand implements BotCommand {

    private static final String COMMAND_PREFIX = "UTC";
    private static final Pattern UTC_PATTERN = Pattern.compile(
            "^UTC([+-]?)(\\d{1,2})(?::(\\d{2}))?$",
            Pattern.CASE_INSENSITIVE);

    private final UserService userService;

    private final BotCommandStorage commandStorage;

    @Override
    public boolean isApply(String message) {
        return message.startsWith(COMMAND_PREFIX);
    }

    @Override
    public SendMessage execute(TelegramUpdateInfo update) {

        Matcher matcher = UTC_PATTERN.matcher(update.getMessage().trim());
        int offsetSeconds = 0;
        if (matcher.find()) {
            int sign = matcher.group(1).equals("-") ? -1 : 1;
            int hours = Integer.parseInt(matcher.group(2));
            int minutes = matcher.group(3) != null
                    ? Integer.parseInt(matcher.group(3)) : 0;
            offsetSeconds = sign * ((hours * 3600) + (minutes * 60));
        }

        final Long chatId = update.getChatId();
        final Long userId = update.getUserId();

        if (userService.exist(userId)) {
            final BotUser user = userService.find(userId);
            user.setOffsetSeconds(offsetSeconds);
            userService.save(user);
        } else {
            final BotUser user = new BotUser(userId, update.getFirstName(), update.getLastName(), update.getUserName(), chatId);
            user.setOffsetSeconds(offsetSeconds);
            userService.save(user);
        }
        final InlineKeyboardButton menuBtn = InlineKeyboardButton.builder()
                .text("Menu").callbackData("/menu").build();
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(offsetSeconds);
        final String zoneId = ZoneId.ofOffset("UTC", offset).toString();
        return SendMessage.builder()
                .text("Для нотификации будет использован часовой пояс %s"
                        .formatted(zoneId))
                .replyMarkup(new InlineKeyboardMarkup(List.of(new InlineKeyboardRow(menuBtn))))
                .chatId(chatId)
                .build();
    }
}
