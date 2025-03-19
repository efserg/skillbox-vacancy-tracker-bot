package com.skillbox.vacancy.tracker.bot.command;

import com.skillbox.vacancy.tracker.bot.BotCommandStorage;
import com.skillbox.vacancy.tracker.bot.TelegramUpdateInfo;
import com.skillbox.vacancy.tracker.model.FindVacancyTask;
import com.skillbox.vacancy.tracker.service.FindVacancyTaskService;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage.SendMessageBuilder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@RequiredArgsConstructor
public class SetRegionCommand implements BotCommand {

    private final FindVacancyTaskService findVacancyTaskService;

    private final BotCommandStorage commandStorage;

    private static final int PAGE_SIZE = 10;

    @Override
    public boolean isApply(String message) {
        return message.startsWith("/region");
    }

    @Override
    public SendMessage execute(TelegramUpdateInfo update) {
        final SendMessageBuilder<?, ?> sendMessageBuilder = SendMessage.builder()
                .text("Выбирайте регион:")
                .chatId(update.getChatId());
        if (!update.hasParams()) {
            return sendMessageBuilder.replyMarkup(getKeyboardMarkup(0)).build();
        }
        final String firstParam = update.getParams().get(0);
        if ("next".equals(firstParam)) {
            final int start = Integer.parseInt(update.getParams().get(1));
            return sendMessageBuilder.replyMarkup(getKeyboardMarkup(start + PAGE_SIZE)).build();
        } else if ("previous".equals(firstParam)) {
            final int start = Integer.parseInt(update.getParams().get(1));
            return sendMessageBuilder.replyMarkup(getKeyboardMarkup(start - PAGE_SIZE)).build();
        } else {
            final Long chatId = update.getChatId();
            final Long userId = update.getUserId();
            final long region = Long.parseLong(firstParam);
            if (findVacancyTaskService.exists(userId, chatId)) {
                final FindVacancyTask task = findVacancyTaskService.find(userId, chatId);
                task.setRegion(region);
                findVacancyTaskService.save(task);
            } else {
                final FindVacancyTask task = FindVacancyTask.builder()
                        .region(region)
                        .userId(userId)
                        .chatId(chatId)
                        .build();
                findVacancyTaskService.save(task);
            }
            return commandStorage.get(MainMenuCommand.class).execute(update);
        }
    }

    @NotNull
    private InlineKeyboardMarkup getKeyboardMarkup(int start) {
        final List<Region> regions = getRegions();
        final InlineKeyboardButton nextBtn = InlineKeyboardButton.builder()
                .text("Следующие " + PAGE_SIZE)
                .callbackData("/region next " + start)
                .build();
        final InlineKeyboardButton prevBtn = InlineKeyboardButton.builder()
                .text("Предыдущие " + PAGE_SIZE)
                .callbackData("/region previous " + start)
                .build();
        final List<InlineKeyboardButton> nextPrevBtns = Stream.of(
                        start <= 0 ? null : prevBtn,
                        start >= (regions.size() - PAGE_SIZE) ? null : nextBtn
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        final List<InlineKeyboardRow> keyboard = Stream.concat(
                regions.stream().skip(start)
                        .map(r -> InlineKeyboardButton.builder()
                                .text(r.number + " " + r.name)
                                .callbackData("/region " + r.number)
                                .build())
                        .map(InlineKeyboardRow::new)
                        .limit(PAGE_SIZE),
                Stream.of(new InlineKeyboardRow(nextPrevBtns))
        ).collect(Collectors.toList());

        return new InlineKeyboardMarkup(keyboard);
    }

    private List<Region> getRegions() {
        return List.of(
                new Region(1, "Республика Адыгея (Адыгея)"),
                new Region(2, "Республика Башкортостан"),
                new Region(3, "Республика Бурятия"),
                new Region(4, "Республика Алтай"),
                new Region(5, "Республика Дагестан"),
                new Region(6, "Республика Ингушетия"),
                new Region(7, "Кабардино-Балкарская Республика"),
                new Region(8, "Республика Калмыкия"),
                new Region(9, "Карачаево-Черкесская Республика"),
                new Region(10, "Республика Карелия"),
                new Region(11, "Республика Коми"),
                new Region(12, "Республика Марий Эл"),
                new Region(13, "Республика Мордовия"),
                new Region(14, "Республика Саха (Якутия)"),
                new Region(15, "Республика Северная Осетия - Алания"),
                new Region(16, "Республика Татарстан (Татарстан)"),
                new Region(17, "Республика Тыва"),
                new Region(18, "Удмуртская Республика"),
                new Region(19, "Республика Хакасия"),
                new Region(20, "Чеченская Республика"),
                new Region(21, "Чувашская Республика - Чувашия"),
                new Region(22, "Алтайский край"),
                new Region(23, "Краснодарский край"),
                new Region(24, "Красноярский край"),
                new Region(25, "Приморский край"),
                new Region(26, "Ставропольский край"),
                new Region(27, "Хабаровский край"),
                new Region(28, "Амурская область"),
                new Region(29, "Архангельская область"),
                new Region(30, "Астраханская область"),
                new Region(31, "Белгородская область"),
                new Region(32, "Брянская область"),
                new Region(33, "Владимирская область"),
                new Region(34, "Волгоградская область"),
                new Region(35, "Вологодская область"),
                new Region(36, "Воронежская область"),
                new Region(37, "Ивановская область"),
                new Region(38, "Иркутская область"),
                new Region(39, "Калининградская область"),
                new Region(40, "Калужская область"),
                new Region(41, "Камчатский край"),
                new Region(42, "Кемеровская область"),
                new Region(43, "Кировская область"),
                new Region(44, "Костромская область"),
                new Region(45, "Курганская область"),
                new Region(46, "Курская область"),
                new Region(47, "Ленинградская область"),
                new Region(48, "Липецкая область"),
                new Region(49, "Магаданская область"),
                new Region(50, "Московская область"),
                new Region(51, "Мурманская область"),
                new Region(52, "Нижегородская область"),
                new Region(53, "Новгородская область"),
                new Region(54, "Новосибирская область"),
                new Region(55, "Омская область"),
                new Region(56, "Оренбургская область"),
                new Region(57, "Орловская область"),
                new Region(58, "Пензенская область"),
                new Region(59, "Пермский край"),
                new Region(60, "Псковская область"),
                new Region(61, "Ростовская область"),
                new Region(62, "Рязанская область"),
                new Region(63, "Самарская область"),
                new Region(64, "Саратовская область"),
                new Region(65, "Сахалинская область"),
                new Region(66, "Свердловская область"),
                new Region(67, "Смоленская область"),
                new Region(68, "Тамбовская область"),
                new Region(69, "Тверская область"),
                new Region(70, "Томская область"),
                new Region(71, "Тульская область"),
                new Region(72, "Тюменская область"),
                new Region(73, "Ульяновская область"),
                new Region(74, "Челябинская область"),
                new Region(75, "Забайкальский край"),
                new Region(76, "Ярославская область"),
                new Region(77, "г. Москва"),
                new Region(78, "Санкт-Петербург"),
                new Region(79, "Еврейская автономная область"),
                new Region(83, "Ненецкий автономный округ"),
                new Region(86, "Ханты-Мансийский автономный округ - Югра"),
                new Region(87, "Чукотский автономный округ"),
                new Region(89, "Ямало-Ненецкий автономный округ"),
                new Region(99, "Иные территории, включая город и космодром Байконур")
        );
    }

    private record Region(int number, String name) {

    }
}
