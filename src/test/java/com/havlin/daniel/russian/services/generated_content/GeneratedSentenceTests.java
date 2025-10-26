package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.generated_content.GeneratedContentError;
import com.havlin.daniel.russian.entities.generated_content.GeneratedSentenceGrammarForm;
import com.havlin.daniel.russian.entities.generated_content.ReadingLevel;
import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import com.havlin.daniel.russian.repositories.generated_content.GeneratedContentErrorRepository;
import com.havlin.daniel.russian.services.dictionary.SentenceService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.mockito.Mockito.mock;

@SpringBootTest
public class GeneratedSentenceTests {
    @Autowired
    private SentenceService sentenceService;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private WordFormRepository wordFormRepository;

    @Autowired
    private GeneratedContentService generatedContentService;

    @Autowired
    private GeneratedContentErrorService generatedContentErrorService;

    @Autowired
    private GeneratedContentErrorRepository generatedContentErrorRepository;

    @Test
    @Transactional
    public void generateContentToDB() {
        Word word = wordRepository.findById(170L).get();
        generatedContentService.generateContentForWord(word, AiModel.GEMINI);
        List<GeneratedContentError> errors = generatedContentErrorRepository.findAllByOriginatingEntityId(170L);

        Assertions.assertAll(
                () -> Assertions.assertFalse(word.getSentences().isEmpty())
        );
    }

    @Test
    @Transactional
    public void testSaveSentencesToDB() {

    }

    @Test
    public void promptGeneration() {
        PromptGenerator promptGenerator = new PromptGenerator(wordFormRepository);
        String promptVerb = promptGenerator.buildPromptForSentenceGeneration(wordRepository.findById(170L).get(), ReadingLevel.BEGINNER);
        String promptNoun = promptGenerator.buildPromptForSentenceGeneration(wordRepository.findById(153L).get(), ReadingLevel.INTERMEDIATE);
        String promptNoun2 = promptGenerator.buildPromptForSentenceGeneration(wordRepository.findById(230L).get(), ReadingLevel.BEGINNER);
        String promptNoun3 = promptGenerator.buildPromptForSentenceGeneration(wordRepository.findById(231L).get(), ReadingLevel.INTERMEDIATE);
        String promptNoun4 = promptGenerator.buildPromptForSentenceGeneration(wordRepository.findById(237L).get(), ReadingLevel.ADVANCED);
        String promptAdjective = promptGenerator.buildPromptForSentenceGeneration(wordRepository.findById(196L).get(), ReadingLevel.ADVANCED);
        String promptAdjective2 = promptGenerator.buildPromptForSentenceGeneration(wordRepository.findById(526L).get(), ReadingLevel.BEGINNER);
        String promptAdjective3 = promptGenerator.buildPromptForSentenceGeneration(wordRepository.findById(531L).get(), ReadingLevel.INTERMEDIATE);
        String promptAdjective4 = promptGenerator.buildPromptForSentenceGeneration(wordRepository.findById(526L).get(), ReadingLevel.INTERMEDIATE);


        Assertions.assertAll(
                () -> Assertions.assertEquals(true, promptVerb.contains("First Person Singular: оста'нусь")),
                () -> Assertions.assertEquals(false, promptVerb.contains("Gerund Present:")),
                () -> Assertions.assertEquals(true, promptVerb.contains("Participle Active Past: оста'вшийся")),
                () -> Assertions.assertEquals(true, promptVerb.contains("B1 level Russian")),
                () -> Assertions.assertEquals(true, promptNoun.contains("Instrumental: зе'млями")),
                () -> Assertions.assertEquals(true, promptNoun.contains("Accusative: зе'млю")),
                () -> Assertions.assertEquals(true, promptNoun.contains("B2 level Russian"))
        );
    }

    @Test
    public void verbSentence() {
        SentenceGenerator sentenceGenerator = new SentenceGenerator(wordFormRepository, generatedContentErrorService);

        String successfulSentences = "Учени'к, оста'вшийся по'сле уро'ков, убира'л класс.\n" +
                "The student who stayed after lessons was cleaning the classroom.\n" +
                "Participle Active Past\n" +
                "Оста'вшись одна' до'ма, де'вочка испуга'лась.\n" +
                "Having been left alone at home, the girl got scared.\n" +
                "Gerund Past\n" +
                "Роди'тели оста'нутся рабо'тать до ве'чера.\n" +
                "The parents will stay to work until evening.\n" +
                "Third Person Plural\n" +
                "Вы оста'нетесь довольны' на'шим о'тдыхом на мо'ре.\n" +
                "You will remain satisfied with our vacation by the sea.\n" +
                "Second Person Plural\n" +
                "Мы оста'немся жить в э'том го'роде.\n" +
                "We will stay living in this city.\n" +
                "First Person Plural\n" +
                "Магази'н оста'нется закры'тым в понеде'льник.\n" +
                "The store will remain closed on Monday.\n" +
                "Third Person Singular\n" +
                "Ты оста'нешься дома' смотре'ть телеви'зор?\n" +
                "Will you stay home to watch television?\n" +
                "Second Person Singular\n" +
                "Я оста'нусь помога'ть ба'бушке в саду'.\n" +
                "I will stay to help grandmother in the garden.\n" +
                "First Person Singular\n" +
                "Де'ти оста'лись игра'ть во дворе'.\n" +
                "The children stayed to play in the yard.\n" +
                "Past\n" +
                "Молоко' оста'лось сто'ять на столе'.\n" +
                "The milk was left standing on the table.\n" +
                "Past\n" +
                "Ма'ма оста'лась го'товить у'жин.\n" +
                "Mom stayed to prepare dinner.\n" +
                "Past\n" +
                "Па'па оста'лся чита'ть га'зету в кре'сле.\n" +
                "Dad stayed to read the newspaper in the armchair.\n" +
                "Past\n" +
                "Оста'ньтесь, пожа'луйста, на чай с пече'ньем.\n" +
                "Please stay for tea with cookies.\n" +
                "Imperative Plural\n" +
                "Оста'нься с на'ми на выходны'е.\n" +
                "Stay with us for the weekend.\n" +
                "Imperative Singular";

        String unsuccessfulSentences = "Here's the comprehensive list of sentences using the word остаться. \n" +
                "Yчени'к, оста'вшийся по'сле уро'ков, убира'л класс.\n" +
                "The student who stayed after lessons was cleaning the classroom.\n" +
                "Participle Active Past\n" +
                "Оста'вшись одна' до'ма, де'вочка испуга'лась.\n" +
                "Having been left alone at home, the girl got scared.\n" +
                "Gerund Past\n" +
                "Роди'тели оста'нутся рабо'тать до ве'чера.\n" +
                "The parents will stay to work until evening.\n" +
                "Third Person Plural\n" +
                "Вы оста'нетесь довольны' на'шим о'тдыхом на mо're.\n" +
                "You will remain satisfied with our vacation by the sea.\n" +
                "Second Person Plural\n" +
                "Мы оста'немся жить в э'том го'роде.\n" +
                "We will stay living in this city.\n" +
                "First Person Plural\n" +
                "Магази'н оста'нется закры'тым в понеде'льник.\n" +
                "The store will remain closed on Monday.\n" +
                "Third Person Singular\n" +
                "Ты оста'нешься дома' смотре'ть телеви'зор?\n" +
                "Will you stay home to watch television?\n" +
                "Second Person Singular\n" +
                "Я оста'нусь помога'ть ба'бушkе в саду'.\n" +
                "I will stay to help grandmother in the garden.\n" +
                "First Person Singular\n" +
                "Де'ти оста'лись игра'ть во дворе'.\n" +
                "The children stayed to play in the yard.\n" +
                "Past\n" +
                "Молоко' оста'лось сто'ять на столе'.\n" +
                "The milk was left standing on the table.\n" +
                "Past\n" +
                "Ма'ма оста'лась го'товить у'жин.\n" +
                "Mom stayed to prepare dinner.\n" +
                "Past\n" +
                "Па'па оста'лся чита'ть га'зетy в кре'сле.\n" +
                "Dad stayed to read the newspaper in the armchair.\n" +
                "Past\n" +
                "Оста'ньтесь, пожа'луйста, на чай c печеньем.\n" +
                "Please stay for tea with cookies.\n" +
                "Imperative Plural\n" +
                "Оста'нься с нами на выходны'е.\n" +
                "Stay with us for the weekend.\n" +
                "Imperative Singular";

        Word word = wordRepository.findWordByIdWithSentencesAndWordForms(170L).get();

        List<GeneratedContentService.SentenceWithErrors> createdSentences = sentenceGenerator.createSentenceListFromGeneratedSentences(successfulSentences, word, ReadingLevel.BEGINNER);
        List<GeneratedContentService.SentenceWithErrors> createdSentencesWithUnsuccessful = sentenceGenerator.createSentenceListFromGeneratedSentences(unsuccessfulSentences, word, ReadingLevel.BEGINNER);


        Assertions.assertAll(
                () -> Assertions.assertEquals(14, createdSentences.size()),
                () -> Assertions.assertEquals(GeneratedSentenceGrammarForm.PARTICIPLE_ACTIVE_PAST, createdSentences.get(0).sentence.getGrammarForm()),
                () -> Assertions.assertEquals("Оста'нься с на'ми на выходны'е.", createdSentences.get(13).sentence.getText()),
                () -> Assertions.assertFalse(createdSentencesWithUnsuccessful.size() == 9),
                () -> Assertions.assertEquals(ReadingLevel.BEGINNER, createdSentencesWithUnsuccessful.get(0).sentence.getReadingLevel()),
                () -> Assertions.assertEquals(1, createdSentences.get(0).sentence.getWordPosition()),
                () -> Assertions.assertEquals(0, createdSentences.get(13).sentence.getWordPosition())
        );
    }

    @Test
    public void nounSentence() {
        SentenceGenerator sentenceGenerator = new SentenceGenerator(wordFormRepository, generatedContentErrorService);

        String nounSentences1 = "В э'тих зе'млях обита'ют ди'кие живо'тные.\n" +
                "Wild animals inhabit these lands.\n" +
                "Prepositional\n" +
                "Кресть'яне торгу'ют зе'млями уже' не'сколько лет.\n" +
                "Peasants have been trading lands for several years.\n" +
                "Instrumental\n" +
                "Прави'тельство разда'ет зе'мли молоды'м се'мьям.\n" +
                "The government distributes lands to young families.\n" +
                "Accusative\n" +
                "Экспе'рты оказы'вают по'мощь зе'млям в засу'шливых райо'нах.\n" +
                "Experts provide assistance to lands in arid regions.\n" +
                "Dative\n" +
                "Площа'дь земе'ль постоя'нно сокраща'ется из-за урбанизации.\n" +
                "The area of lands is constantly decreasing due to urbanization.\n" +
                "Genitive\n" +
                "Зе'мли на се'вере стра'ны остаю'тся незасе'янными.\n" +
                "Lands in the north of the country remain unseeded.\n" +
                "Nominative\n" +
                "Учё'ные изуча'ют микрооргани'змы в э'той земле'.\n" +
                "Scientists study microorganisms in this soil.\n" +
                "Prepositional\n" +
                "Са'довник удобря'ет расте'ния богаа'той землёй.\n" +
                "The gardener fertilizes plants with rich soil.\n" +
                "Instrumental\n" +
                "Ста'рые ле'генды свя'заны с э'той свяще'нной землёю.\n" +
                "Old legends are connected with this sacred land.\n" +
                "Instrumental\n" +
                "Рабо'чие перево'зят зе'млю в больши'х грузови'ках.\n" +
                "Workers transport soil in large trucks.\n" +
                "Accusative\n" +
                "Строи'тели прида'ют осо'бое внима'ние земле' под фунда'ментом.\n" +
                "Builders pay special attention to the ground under the foundation.\n" +
                "Dative\n" +
                "Фе'рмер изуча'ет соста'в э'той земли' пе'ред посе'вом.\n" +
                "The farmer studies the composition of this soil before sowing.\n" +
                "Genitive\n" +
                "Земля' враща'ется вокру'г свое'й о'си.\n" +
                "Earth rotates around its axis.\n" +
                "Nominative";
            Word word1 = wordRepository.findWordByIdWithSentencesAndWordForms(153L).get();
            List<GeneratedContentService.SentenceWithErrors> createdSentences1 = sentenceGenerator.createSentenceListFromGeneratedSentences(nounSentences1, word1, ReadingLevel.INTERMEDIATE);

            String nounSentences2 = "В э'тих ко'мнатах жи'ли студе'нты университе'та.\n" +
                    "University students lived in these rooms.\n" +
                    "Prepositional\n" +
                    "Дизайнер управля'ет ко'мнатами с по'мощью у'мных техноло'гий.\n" +
                    "The designer manages rooms using smart technologies.\n" +
                    "Instrumental\n" +
                    "Горни'чная убира'ет ко'мнаты ка'ждое у'тро.\n" +
                    "The maid cleans rooms every morning.\n" +
                    "Accusative\n" +
                    "Архите'ктор прида'ёт осо'бый стиль ко'мнатам в до'ме.\n" +
                    "The architect gives a special style to the rooms in the house.\n" +
                    "Dative\n" +
                    "Коли́чество ко'мнат в кварти'ре зави'сит от её площа'ди.\n" +
                    "The number of rooms in an apartment depends on its area.\n" +
                    "Genitive\n" +
                    "Ко'мнаты в но'вом до'ме о'чень просто́рные.\n" +
                    "The rooms in the new house are very spacious.\n" +
                    "Nominative\n" +
                    "В э'той ко'мнате стои́т ста'ринная ме́бель.\n" +
                    "Antique furniture stands in this room.\n" +
                    "Prepositional\n" +
                    "Де́ти игра́ют с игру́шками в ко'мнате ка'ждый день.\n" +
                    "Children play with toys in the room every day.\n" +
                    "Instrumental\n" +
                    "Ба́бушка расска́зывала ска́зки в э'той уго́тной ко'мнатою.\n" +
                    "Grandmother told fairy tales in this cozy room.\n" +
                    "Instrumental\n" +
                    "Сту́денты снима́ют ко'мнату ря́дом с университе́том.\n" +
                    "Students rent a room near the university.\n" +
                    "Accusative\n" +
                    "Роди́тели да́рят но́вую ме́бель ко'мнате сы́на.\n" +
                    "Parents give new furniture to their son's room.\n" +
                    "Dative\n" +
                    "Окна́ э'той ко'мнаты выхо́дят на юг.\n" +
                    "The windows of this room face south.\n" +
                    "Genitive\n" +
                    "Ко'мната для госте́й всегда́ го́това для прие́ма.\n" +
                    "The guest room is always ready for visitors.\n" +
                    "Nominative";

            Word word2 = wordRepository.findWordByIdWithSentencesAndWordForms(230L).get();
            List<GeneratedContentService.SentenceWithErrors> createdSentences2 = sentenceGenerator.createSentenceListFromGeneratedSentences(nounSentences2, word2, ReadingLevel.BEGINNER);

        String nounSentences3 = "В э'тих у'трах всегда' слы'шно пе'ние птиц.\n" +
                    "In these mornings you can always hear birds singing.\n" +
                    "Prepositional\n" +
                    "В холо'дных утра'х зимы' осо'бенно чу'вствуется све'жесть.\n" +
                    "In the cold mornings of winter, freshness is especially felt.\n" +
                    "Prepositional\n" +
                    "Рабо'чие управля'ют у'трами бла'годаря че'ткому распи'санию.\n" +
                    "Workers manage mornings thanks to a clear schedule.\n" +
                    "Instrumental\n" +
                    "Учителя' планируют уро'ки утра'ми выходны'х дней.\n" +
                    "Teachers plan lessons during weekend mornings.\n" +
                    "Instrumental\n" +
                    "Спорт'смен трениру'ется ра'нние у'тра пе'ред соревнования'ми.\n" +
                    "The athlete trains early mornings before competitions.\n" +
                    "Accusative\n" +
                    "Врач рекоме'ндует прогу'лки у'трам для лу'чшего самочу'вствия.\n" +
                    "The doctor recommends walks for mornings for better well-being.\n" +
                    "Dative\n" +
                    "Медсестра' де'лает обхо'д утра'м в больни'це.\n" +
                    "The nurse makes rounds in the mornings at the hospital.\n" +
                    "Dative\n" +
                    "Коли'чество утр в ме'сяце зави'сит от его' продолжи'тельности.\n" +
                    "The number of mornings in a month depends on its duration.\n" +
                    "Genitive\n" +
                    "У'тра в го'роде всегда' начина'ются с шу'ма тра'нспорта.\n" +
                    "Mornings in the city always begin with the noise of transport.\n" +
                    "Nominative\n" +
                    "В ра'ннем у'тре во'здух осо'бенно чист и све'ж.\n" +
                    "In the early morning the air is especially clean and fresh.\n" +
                    "Prepositional\n" +
                    "Фе'рмер встаё'т у'тром для ухо'да за живо'тными.\n" +
                    "The farmer gets up in the morning to care for animals.\n" +
                    "Instrumental\n" +
                    "Студе'нты изуча'ют у'тро как вре'мя наивы'сшей продукти'вности.\n" +
                    "Students study morning as a time of highest productivity.\n" +
                    "Accusative\n" +
                    "Психо'лог сове'тует удели'ть внима'ние у'тру для планиро'вания дня.\n" +
                    "The psychologist advises paying attention to morning for planning the day.\n" +
                    "Dative\n" +
                    "Врач изуча'ет симпто'мы утру' для то'чной диагно'стики.\n" +
                    "The doctor studies morning symptoms for accurate diagnosis.\n" +
                    "Dative\n" +
                    "Красота' у'тра вдохновля'ет мно'гих худо'жников.\n" +
                    "The beauty of morning inspires many artists.\n" +
                    "Genitive\n" +
                    "Красота' утра' в горах захва'тывает дыха'ние.\n" +
                    "The beauty of morning in the mountains takes your breath away.\n" +
                    "Genitive\n" +
                    "У'тро начина'ется с пе'рвых луче'й со'лнца.\n" +
                    "Morning begins with the first rays of sun.\n" +
                    "Nominative";

            Word word3 = wordRepository.findWordByIdWithSentencesAndWordForms(231L).get();
            List<GeneratedContentService.SentenceWithErrors> createdSentences3 = sentenceGenerator.createSentenceListFromGeneratedSentences(nounSentences3, word3, ReadingLevel.INTERMEDIATE);


        String nounSentences4 = "На широ'ких плеча'х атле'та заме'тны сле'ды интенси'вных трениро'вок.\n" +
                    "On the broad shoulders of the athlete, traces of intensive training are noticeable.\n" +
                    "Prepositional\n" +
                    "Хиру'рг мани'пулирует плеча'ми паци'ента с исключи'тельной то'чностью.\n" +
                    "The surgeon manipulates the patient's shoulders with exceptional precision.\n" +
                    "Instrumental\n" +
                    "Ортопе'д обсле'дует пле'чи спортсме'на по'сле тра'вмы.\n" +
                    "The orthopedist examines the athlete's shoulders after the injury.\n" +
                    "Accusative\n" +
                    "Физиотерапе'вт разраба'тывает индивидуа'льные упражне'ния плеча'м пожилы'х пацие'нтов.\n" +
                    "The physiotherapist develops individual exercises for elderly patients' shoulders.\n" +
                    "Dative\n" +
                    "Анато'мия пле'ч представля'ет собо'й сло'жную систе'му мышц и су'ставов.\n" +
                    "The anatomy of shoulders represents a complex system of muscles and joints.\n" +
                    "Genitive\n" +
                    "Пле'чи балери'ны де'монстрируют идеа'льную о'санку и гра'цию.\n" +
                    "The ballerina's shoulders demonstrate perfect posture and grace.\n" +
                    "Nominative\n" +
                    "На боле'зненном плече' пацие'нта образова'лась припу'хлость.\n" +
                    "A swelling has formed on the patient's painful shoulder.\n" +
                    "Prepositional\n" +
                    "Масса'жист рабо'тает с напря'жённым плечо'м клие'нта.\n" +
                    "The massage therapist works with the client's tense shoulder.\n" +
                    "Instrumental\n" +
                    "Врач тща'тельно о'щупывает повреждённое плечо' для постано'вки диа'гноза.\n" +
                    "The doctor carefully palpates the damaged shoulder to make a diagnosis.\n" +
                    "Accusative\n" +
                    "Реабилито'лог уделя'ет осо'бое внима'ние травми'рованному плечу'.\n" +
                    "The rehabilitation specialist pays special attention to the injured shoulder.\n" +
                    "Dative\n" +
                    "Подви'жность плеча' ограни'чена из-за воспали'тельного проце'сса.\n" +
                    "The mobility of the shoulder is limited due to the inflammatory process.\n" +
                    "Genitive\n" +
                    "Плечо' челове'ка спосо'бно выполня'ть сло'жные ротацио'нные движе'ния.\n" +
                    "The human shoulder is capable of performing complex rotational movements.\n" +
                    "Nominative";

            Word word4 = wordRepository.findWordByIdWithSentencesAndWordForms(237L).get();
            List<GeneratedContentService.SentenceWithErrors> createdSentences4 = sentenceGenerator.createSentenceListFromGeneratedSentences(nounSentences4, word4, ReadingLevel.ADVANCED);

            String nounSentences5 = "Пр'и изуче'нии иностр'анных язы'ков мы' часто' занима'емся в ко'мнатах с совреме'нным обор'удованием.\n" +
                    "When studying foreign languages, we often work in rooms with modern equipment.\n" +
                    "Prepositional\n" +
                    "\n" +
                    "Зд'ание бы'ло пер'еполнено ра'зными ко'мнатами с нео'бычной планир'овкой.\n" +
                    "The building was filled with various rooms with unusual layouts.\n" +
                    "Prepositional\n" +
                    "\n" +
                    "Архите'ктор управл'ял ко'мнатами так иску'сно, что каза'лось, буд'то прострáнство танц'ует.\n" +
                    "The architect managed rooms so skillfully that it seemed as if space was dancing.\n" +
                    "Instrumental\n" +
                    "\n" +
                    "Мы любова'лись инте'рьерами, созда'нными опы'тными ко'мнатами.\n" +
                    "We admired the interiors created by experienced rooms.\n" +
                    "Instrumental\n" +
                    "\n" +
                    "Дизайн'еры подгот'овили нео'бычные ко'мнаты для межд}'народной выст'авки.\n" +
                    "Designers prepared unusual rooms for the international exhibition.\n" +
                    "Accusative\n" +
                    "\n" +
                    "Я еле' зам'етил ста'рые ко'мнаты в глубин'е музе'я.\n" +
                    "I barely noticed the old rooms deep in the museum.\n" +
                    "Accusative\n" +
                    "\n" +
                    "Пр'и планир'овании ремо'нта мы уделя'ем особое внима'ние ко'мнатам.\n" +
                    "When planning repairs, we pay special attention to rooms.\n" +
                    "Dative\n" +
                    "\n" +
                    "Архите'кторы посвят'или ко'мнатам це'лый симпо'зиум.\n" +
                    "Architects dedicated an entire symposium to rooms.\n" +
                    "Dative\n" +
                    "\n" +
                    "В старом до'ме бы'ло мно'го ко'мнат с таи'нственной истор'ией.\n" +
                    "In the old house, there were many rooms with mysterious histories.\n" +
                    "Genitive\n" +
                    "\n" +
                    "Мы исследова'ли архитект'уру, изуча'я структ'уру ко'мнат.\n" +
                    "We studied architecture by examining the structure of rooms.\n" +
                    "Genitive\n" +
                    "\n" +
                    "Про'сторные ко'мнаты созда'ют ощуще'ние свобо'ды.\n" +
                    "Spacious rooms create a sense of freedom.\n" +
                    "Nominative\n" +
                    "\n" +
                    "Ста'ринные ко'мнаты храня'т дух ми'нувших эпох.\n" +
                    "Ancient rooms preserve the spirit of past eras.\n" +
                    "Nominative\n" +
                    "\n" +
                    "Мы обсужда'ли инте'рьер в ко'мнате с панор'амным вид'ом.\n" +
                    "We discussed the interior in a room with a panoramic view.\n" +
                    "Prepositional\n" +
                    "\n" +
                    "В ко'мнате цар'ила особая творч'еская атмосф'ера.\n" +
                    "A special creative atmosphere reigned in the room.\n" +
                    "Prepositional\n" +
                    "\n" +
                    "Писа'тель владе'л ко'мнатой с невероя'тной лёгкостью.\n" +
                    "The writer possessed the room with incredible ease.\n" +
                    "Instrumental\n" +
                    "\n" +
                    "Мы любова'лись ко'мнатою, где каждая дет'аль бы'ла прод'умана.\n" +
                    "We admired the room where every detail was carefully considered.\n" +
                    "Instrumental\n" +
                    "\n" +
                    "Я зашёл в ту ко'мнату, где когда'-то прожива'л извес'тный художник.\n" +
                    "I entered the room where a famous artist once lived.\n" +
                    "Accusative\n" +
                    "\n" +
                    "Мы тщат'ельно обсле'довали ко'мнату в по'исках улик.\n" +
                    "We carefully examined the room in search of clues.\n" +
                    "Accusative\n" +
                    "\n" +
                    "Архите'ктор переда'л ключ'и ко'мнате в но'вом зд'ании.\n" +
                    "The architect handed over the keys to the room in the new building.\n" +
                    "Dative\n" +
                    "\n" +
                    "Мы посвят'или ко'мнате це'лый день реставр'ации.\n" +
                    "We dedicated an entire day to the room's restoration.\n" +
                    "Dative\n" +
                    "\n" +
                    "Стар'инная ме'бель ко'мнаты расск'азывала свою' исто'рию.\n" +
                    "The antique furniture of the room told its story.\n" +
                    "Genitive\n" +
                    "\n" +
                    "Атмосф'ера ко'мнаты бы'ла напол'нена воспомина'ниями.\n" +
                    "The room's atmosphere was filled with memories.\n" +
                    "Genitive\n" +
                    "\n" +
                    "Ко'мната каза'лась живо'й и дыш'ащей.\n" +
                    "The room seemed alive and breathing.\n" +
                    "Nominative\n" +
                    "\n" +
                    "Ко'мната встрет'ила нас нео'бычным све'том.\n" +
                    "The room greeted us with unusual light.\n" +
                    "Nominative";

        Word word5 = wordRepository.findWordByIdWithSentencesAndWordForms(230L).get();
        List<GeneratedContentService.SentenceWithErrors> createdSentences5 = sentenceGenerator.createSentenceListFromGeneratedSentences(nounSentences5, word5, ReadingLevel.ADVANCED);


        Assertions.assertAll(
                () -> Assertions.assertEquals(13, createdSentences1.size()),
                () -> Assertions.assertEquals(13, createdSentences2.size()),
                () -> Assertions.assertEquals(17, createdSentences3.size()),
                () -> Assertions.assertEquals(12, createdSentences4.size())
            );
    }

    @Test
    public void adjectiveSentences() {
        SentenceGenerator sentenceGenerator = new SentenceGenerator(wordFormRepository, generatedContentErrorService);

        String sentence1 = "Here's the comprehensive set of sentences with the requested specifications:\n" +
                "В диску'ссии о гла'вных достиже'ниях совреме'нной нейробиоло'гии приня'ли уча'стие веду'щие специали'сты.\n" +
                "Leading specialists participated in the discussion about the main achievements of modern neurobiology.\n" +
                "Prepositional\n" +
                "Режиссёр воплоти'л свою' конце'пцию гла'вными вырази'тельными сре'дствами кинематогра'фа.\n" +
                "The director embodied his concept through the main expressive means of cinematography.\n" +
                "Instrumental\n" +
                "Анали'тики выдели'ли гла'вные тенде'нции разви'тия мирово'го ры'нка криптовалю'т.\n" +
                "Analysts identified the main trends in the development of the global cryptocurrency market.\n" +
                "Accusative\n" +
                "Сле'дствие допроси'ло гла'вных свиде'телей по де'лу о корру'пции в высших эшело'нах вла'сти.\n" +
                "The investigation interrogated the main witnesses in the case of corruption in the highest echelons of power.\n" +
                "Accusative\n" +
                "Прави'тельство предоста'вило субси'дии гла'вным производи'телям возобновля'емой эне'ргии.\n" +
                "The government provided subsidies to the main producers of renewable energy.\n" +
                "Dative\n" +
                "Экспе'ртиза гла'вных пара'метров климати'ческой моде'ли вы'явила серьёзные погре'шности.\n" +
                "The examination of the main parameters of the climate model revealed serious errors.\n" +
                "Genitive\n" +
                "Гла'вные персона'жи рома'на воплоща'ют архетипи'ческие о'бразы коллекти'вного бессозна'тельного.\n" +
                "The main characters of the novel embody archetypal images of the collective unconscious.\n" +
                "Nominative\n" +
                "Фило'соф размышля'л о гла'вном парадо'ксе челове'ческого существова'ния в э'поху цифровиза'ции.\n" +
                "The philosopher reflected on the main paradox of human existence in the age of digitization.\n" +
                "Prepositional\n" +
                "Хиру'рг провёл опера'цию гла'вным инновацио'нным ме'тодом лапароскопи'и.\n" +
                "The surgeon performed the operation using the main innovative method of laparoscopy.\n" +
                "Instrumental\n" +
                "Коми'ссия утверди'ла гла'вное направле'ние нау'чно-иссле'довательской де'ятельности институ'та.\n" +
                "The commission approved the main direction of the institute's research activities.\n" +
                "Accusative\n" +
                "Секрета'рь доложи'л гла'вному координа'тору прое'кта о возни'кших тру'дностях.\n" +
                "The secretary reported the arising difficulties to the main project coordinator.\n" +
                "Dative\n" +
                "Значе'ние гла'вного постула'та ква'нтовой меха'ники до сих пор вызыва'ет спо'ры.\n" +
                "The meaning of the main postulate of quantum mechanics still causes disputes.\n" +
                "Genitive\n" +
                "Гла'вное противоре'чие совреме'нной цивилиза'ции заключа'ется в конфли'кте ме'жду прогре'ссом и эколо'гией.\n" +
                "The main contradiction of modern civilization lies in the conflict between progress and ecology.\n" +
                "Nominative\n" +
                "Кот филосо'фски созерца'л мир из гла'вной ба'шни средневеко'вого за'мка.\n" +
                "The cat philosophically contemplated the world from the main tower of the medieval castle.\n" +
                "Prepositional\n" +
                "Дириже'р управля'л орке'стром гла'вной па'лочкой, кото'рая неко'гда принадлежа'ла Бетхо'вену.\n" +
                "The conductor led the orchestra with the main baton that once belonged to Beethoven.\n" +
                "Instrumental\n" +
                "Поэ'т воспе'л гла'вною му'зой своего' тво'рчества абстра'ктную иде'ю красоты'.\n" +
                "The poet praised the abstract idea of beauty as the main muse of his work.\n" +
                "Instrumental\n" +
                "Детекти'в иска'л гла'вную ули'ку в са'мом неожи'данном ме'сте - холоди'льнике подозрева'емого.\n" +
                "The detective searched for the main evidence in the most unexpected place - the suspect's refrigerator.\n" +
                "Accusative\n" +
                "Изда'тель посвяти'л антоло'гию гла'вной покрови'тельнице иску'сств на'шего вре'мени.\n" +
                "The publisher dedicated the anthology to the main patroness of the arts of our time.\n" +
                "Dative\n" +
                "Интерпрета'ция гла'вной те'мы симфо'нии оказа'лась революцио'нной для музыка'льного ми'ра.\n" +
                "The interpretation of the main theme of the symphony turned out to be revolutionary for the musical world.\n" +
                "Genitive\n" +
                "Гла'вная диле'мма постмодерни'стской эсте'тики - э'то пробле'ма подли'нности в э'поху симуля'кров.\n" +
                "The main dilemma of postmodernist aesthetics is the problem of authenticity in the age of simulacra.\n" +
                "Nominative\n" +
                "Астроно'м обнару'жил но'вую пла'нету в гла'вном скопле'нии тума'нности Андроме'ды.\n" +
                "The astronomer discovered a new planet in the main cluster of the Andromeda nebula.\n" +
                "Prepositional\n" +
                "А'лхимик экспериме'нтировал с элеме'нтами гла'вным филосо'фским ка'мнем средневеко'вья.\n" +
                "The alchemist experimented with elements using the main philosopher's stone of the Middle Ages.\n" +
                "Instrumental\n" +
                "Комите'т одо'брил гла'вный при'нцип эти'ческой экспе'ртизы генети'ческих иссле'дований.\n" +
                "The committee approved the main principle of ethical expertise of genetic research.\n" +
                "Accusative\n" +
                "Сле'дователь предъяви'л обвине'ние гла'вному организа'тору фина'нсовой пирами'ды.\n" +
                "The investigator brought charges against the main organizer of the financial pyramid.\n" +
                "Accusative\n" +
                "Кура'тор предста'вил прое'кт гла'вному мецена'ту совреме'нного иску'сства.\n" +
                "The curator presented the project to the main patron of contemporary art.\n" +
                "Dative\n" +
                "Ана'лиз гла'вного алгори'тма иску'сственного интелле'кта за'нял три ме'сяца.\n" +
                "The analysis of the main artificial intelligence algorithm took three months.\n" +
                "Genitive\n" +
                "Гла'вный вы'зов на'шей э'похи - э'то преодоле'ние цифрово'го нера'венства в о'бществе.\n" +
                "The main challenge of our era is overcoming digital inequality in society.\n" +
                "Nominative\n" +
                "Все аспе'кты пробле'мы гла'вны для понима'ния глуби'нных проце'ссов.\n" +
                "All aspects of the problem are main for understanding the deep processes.\n" +
                "Short form\n" +
                "Э'то положе'ние гла'вно в конте'ксте совреме'нной геополити'ческой ситуа'ции.\n" +
                "This provision is main in the context of the current geopolitical situation.\n" +
                "Short form\n" +
                "Роль иску'сства в о'бществе всегда' была' и остаётся главна'.\n" +
                "The role of art in society has always been and remains main.\n" +
                "Short form\n" +
                "Моме'нт и'стины в нау'чном откры'тии всегда' гла'вен и неповтори'м.\n" +
                "The moment of truth in scientific discovery is always main and unique.\n" +
                "Short form\n" +
                "Э'тот манускри'пт - главне'йший среди всех палимпсе'стов библиоте'ки.\n" +
                "This manuscript is the most main among all the palimpsests in the library.\n" +
                "Superlative\n" +
                "Сего'дняшнее реше'ние сове'та дире'кторов главне'е всех преды'дущих постановле'ний.\n" +
                "Today's decision by the board of directors is more main than all previous resolutions.\n" +
                "Comparative";
        Word word1 = wordRepository.findById(196L).get();
        List<GeneratedContentService.SentenceWithErrors> createdSentences1 = sentenceGenerator.createSentenceListFromGeneratedSentences(sentence1, word1, ReadingLevel.ADVANCED);

        String sentence2 = "В обсужде'нии основны'х страте'гий разви'тия компа'нии уча'ствовали все менедже'ры.\n" +
                "All managers participated in the discussion of the basic development strategies of the company.\n" +
                "Prepositional\n" +
                "Програ'ммист реши'л зада'чу основны'ми алгори'тмами маши'нного обуче'ния.\n" +
                "The programmer solved the problem using basic machine learning algorithms.\n" +
                "Instrumental\n" +
                "Студе'нты изуча'ют основны'е принци'пы кванто'вой фи'зики на тре'тьем ку'рсе.\n" +
                "Students study the basic principles of quantum physics in their third year.\n" +
                "Accusative\n" +
                "Поли'ция задержа'ла основны'х подозрева'емых в киберпреступле'нии.\n" +
                "The police detained the main suspects in the cybercrime.\n" +
                "Accusative\n" +
                "Руково'дство предоста'вило до'ступ основны'м сотру'дникам к секре'тным докуме'нтам.\n" +
                "Management provided access to secret documents for the key employees.\n" +
                "Dative\n" +
                "Ана'лиз основны'х показа'телей экономи'ческого ро'ста выяви'л трево'жные тенде'нции.\n" +
                "The analysis of basic economic growth indicators revealed alarming trends.\n" +
                "Genitive\n" +
                "Основны'е элеме'нты архитекту'рного прое'кта вы'глядят весьма' инновацио'нно.\n" +
                "The basic elements of the architectural project look quite innovative.\n" +
                "Nominative\n" +
                "Филосо'ф размышля'л об основно'м смы'сле челове'ческого существова'ния.\n" +
                "The philosopher reflected on the fundamental meaning of human existence.\n" +
                "Prepositional\n" +
                "Инжене'р констру'ирует мост основны'м ме'тодом компью'терного моделиро'вания.\n" +
                "The engineer designs the bridge using the basic method of computer modeling.\n" +
                "Instrumental\n" +
                "Комите'т утверди'л основно'е направле'ние нау'чных иссле'дований на сле'дующий год.\n" +
                "The committee approved the main direction of scientific research for the next year.\n" +
                "Accusative\n" +
                "Дире'ктор сообщи'л основно'му инве'стору о пла'нах расшире'ния би'знеса.\n" +
                "The director informed the main investor about business expansion plans.\n" +
                "Dative\n" +
                "Цель основно'го экспериме'нта - изуче'ние поведе'ния части'ц в магни'тном по'ле.\n" +
                "The goal of the main experiment is to study particle behavior in a magnetic field.\n" +
                "Genitive\n" +
                "Основно'е препя'тствие для реализа'ции прое'кта - недоста'ток финанси'рования.\n" +
                "The main obstacle to project implementation is lack of funding.\n" +
                "Nominative\n" +
                "Кот спрята'лся в основно'й библиоте'ке, где никто' не мог его' найти'.\n" +
                "The cat hid in the main library where no one could find him.\n" +
                "Prepositional\n" +
                "Ху'дожник рису'ет карти'ну основно'й ки'стью, кото'рая слу'жит ему' уже' де'сять лет.\n" +
                "The artist paints the picture with his main brush, which has served him for ten years already.\n" +
                "Instrumental\n" +
                "Балери'на исполня'ет основно'ю па'ртию в нача'льной сце'не бале'та.\n" +
                "The ballerina performs the main part in the opening scene of the ballet.\n" +
                "Instrumental\n" +
                "Журнали'сты ищу'т основну'ю се'нсацию для за'втрашнего вы'пуска но'востей.\n" +
                "Journalists are looking for the main sensation for tomorrow's news broadcast.\n" +
                "Accusative\n" +
                "Изда'тель посвяти'л кни'гу основно'й вдохнови'тельнице своего' тво'рчества.\n" +
                "The publisher dedicated the book to the main inspiration of his work.\n" +
                "Dative\n" +
                "Интерпрета'ция основно'й те'мы рома'на вы'звала жа'ркие деба'ты среди' кри'тиков.\n" +
                "The interpretation of the main theme of the novel sparked heated debates among critics.\n" +
                "Genitive\n" +
                "Основна'я пробле'ма совреме'нного о'бщества - э'то отчужде'ние люде'й друг от дру'га.\n" +
                "The main problem of modern society is the alienation of people from each other.\n" +
                "Nominative\n" +
                "Астроно'м обнару'жил но'вую звезду' в основно'м созве'здии Се'верного полуша'рия.\n" +
                "The astronomer discovered a new star in the main constellation of the Northern Hemisphere.\n" +
                "Prepositional\n" +
                "Повар гото'вит фирме'нное блю'до основны'м секре'тным ингредие'нтом.\n" +
                "The chef prepares the signature dish with the main secret ingredient.\n" +
                "Instrumental\n" +
                "Комите'т рассма'тривает основно'й вопро'с о рефо'рме образова'тельной систе'мы.\n" +
                "The committee considers the main question about education system reform.\n" +
                "Accusative\n" +
                "Сле'дователь допроси'л основно'го свиде'теля по де'лу о кра'же драгоце'нностей.\n" +
                "The investigator questioned the main witness in the jewelry theft case.\n" +
                "Accusative\n" +
                "Секрета'рь переда'л ва'жную информа'цию основно'му координа'тору прое'кта.\n" +
                "The secretary transmitted important information to the main project coordinator.\n" +
                "Dative\n" +
                "Результа'т основно'го иссле'дования превзошёл все ожида'ния учёных.\n" +
                "The result of the main research exceeded all expectations of the scientists.\n" +
                "Genitive\n" +
                "Основно'й при'нцип рабо'ты на'шей компа'нии - э'то че'стность и прозра'чность.\n" +
                "The main principle of our company's work is honesty and transparency.\n" +
                "Nominative";
        Word word2 = wordRepository.findById(531L).get();
        List<GeneratedContentService.SentenceWithErrors> createdSentences2 = sentenceGenerator.createSentenceListFromGeneratedSentences(sentence2, word2, ReadingLevel.INTERMEDIATE);

        String sentence3 = "Here's a list of sentences that you wanted:" +
                "Я надел се'рый шарф, чтобы согреться в холодный день.\n" +
                "I put on a gray scarf to warm up on a cold day.\n" +
                "Nominative\n" +
                "\n" +
                "В шкафу висит се'рая куртка моего' друга.\n" +
                "A gray jacket of my friend hangs in the closet.\n" +
                "Nominative\n" +
                "\n" +
                "Мы говорили о се'ром коте', который живёт во дворе'.\n" +
                "We were talking about the gray cat that lives in the yard.\n" +
                "Prepositional\n" +
                "\n" +
                "Пёс игра'ет с се'рыми игру'шками на по'ле.\n" +
                "The dog is playing with gray toys in the field.\n" +
                "Instrumental\n" +
                "\n" +
                "Я купи'л се'рые ботинки в магази'не.\n" +
                "I bought gray shoes in the store.\n" +
                "Accusative\n" +
                "\n" +
                "Свет падает на се'рое обла'ко в небе'.\n" +
                "Light falls on the gray cloud in the sky.\n" +
                "Accusative\n" +
                "\n" +
                "Я дал се'рому коту' молока'.\n" +
                "I gave milk to the gray cat.\n" +
                "Dative\n" +
                "\n" +
                "У меня' есть не'сколько се'рых книг.\n" +
                "I have several gray books.\n" +
                "Genitive\n" +
                "\n" +
                "Се'рые волки бега'ют по ле'су.\n" +
                "Gray wolves run through the forest.\n" +
                "Nominative\n" +
                "\n" +
                "Мы разговарива'ли в се'ром каби'нете.\n" +
                "We were talking in the gray office.\n" +
                "Prepositional\n" +
                "\n" +
                "Он управля'ет се'рым автомоби'лем.\n" +
                "He drives a gray car.\n" +
                "Instrumental\n" +
                "\n" +
                "Се'рое небо предвеща'ет дождь.\n" +
                "The gray sky predicts rain.\n" +
                "Nominative\n" +
                "\n" +
                "Я пода'рил се'рому дру'гу интере'сную кни'гу.\n" +
                "I gave an interesting book to the gray friend.\n" +
                "Dative\n" +
                "\n" +
                "Я увиде'л се'рого кота' во дворе'.\n" +
                "I saw a gray cat in the yard.\n" +
                "Accusative\n" +
                "\n" +
                "Она' восхища'ется се'рой ку'рткой.\n" +
                "She admires the gray jacket.\n" +
                "Genitive\n" +
                "\n" +
                "Се'рая кошка спит на по'душке.\n" +
                "The gray cat is sleeping on the pillow.\n" +
                "Nominative\n" +
                "\n" +
                "Мы говори'ли о се'рой по'годе.\n" +
                "We were talking about the gray weather.\n" +
                "Prepositional\n" +
                "\n" +
                "Он рабо'тает с се'рой краской.\n" +
                "He works with gray paint.\n" +
                "Instrumental\n" +
                "\n" +
                "Зве'ри сера.\n" +
                "Animals are gray.\n" +
                "Short\n" +
                "\n" +
                "Не'бо се'ро.\n" +
                "The sky is gray.\n" +
                "Short\n" +
                "\n" +
                "Обла'ка сера'.\n" +
                "Clouds are gray.\n" +
                "Short\n" +
                "\n" +
                "Лес сер.\n" +
                "The forest is gray.\n" +
                "Short";

        Word word3 = wordRepository.findById(526L).get();
        List<GeneratedContentService.SentenceWithErrors> createdSentences3 = sentenceGenerator.createSentenceListFromGeneratedSentences(sentence3, word3, ReadingLevel.BEGINNER);


        Assertions.assertAll(
                () -> Assertions.assertEquals(33, createdSentences1.size()),
                () -> Assertions.assertEquals(27, createdSentences2.size())
        );
    }

    @Test
    public void mockClaudeCall() {

        //        // Mock the client
//        AnthropicClient mockClient = mock(AnthropicClient.class);
//
//        // Mock the response
//        MessageResponse mockResponse = mock(MessageResponse.class);
//        when(mockResponse.getContent()).thenReturn("Mocked sentence response");
//
//        // Configure mock behavior
//        when(mockClient.messages(any())).thenReturn(mockResponse);
//
//        // Use mock in your test
//        CountDownLatch latch = new CountDownLatch(1);
//        ClaudeContentGenerator generator = new ClaudeContentGenerator(latch, mockClient, 0);
//
//        String result = generator.call();
//
//        assertEquals("Mocked sentence response", result);
//        verify(mockClient, times(1)).messages(any());
    }
}
