package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.generated_content.Definition;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import com.havlin.daniel.russian.services.retrieval.WordRetrievalService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

@SpringBootTest
public class GeneratedDefinitionTests {
    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private WordFormRepository wordFormRepository;

    @Autowired
    private GeneratedContentErrorService generatedContentErrorService;

    @Autowired
    private WordRetrievalService wordRetrievalService;

    @Test
    @Transactional
    public void createDefinitionsForWord() {
        DefinitionGenerator definitionGenerator = new DefinitionGenerator(wordRetrievalService);
        PromptGenerator promptGenerator = new PromptGenerator(wordFormRepository);
        Word word = wordRepository.findById(179L).get();
        Word room = wordRepository.findById(230L).get();

        String shortDefinition = "Выполня'ть де'йствия и́ли зада'ния, что'бы получи'ть результа'т и́ли де'ньги.\n" +
                "Занима'ться каки'м-то де'лом и́ли испо́льзовать свои́ уме'ния для по'льзы.\n" +
                "Функциони'ровать и́ли де'йствовать, когда́ речь идёт о механи'зме и́ли устро'йстве.";

        String shortDefinitionWithPreamble = "Here are three B1-level Russian definitions for ко'мната:\n" +
                "\n" +
                "Ча'сть жило'го до'ма, где живу'т и отдыха'ют лю'ди.\n" +
                "Простра'нство с окно'м и две'рью, где мо'жно спа'ть, рабо'тать или игра'ть.\n" +
                "Ме'сто в кварти'ре с по'лом, стена'ми и потолко'м, где хра'нятся ве'щи и живу'т люди'.";

        String wordInformation = "Э'то сло'во обознача'ет проце'сс, когда́ челове'к и́ли предме'т выполня'ет каки'е-то де'йствия, зада'ния и́ли фу́нкции. Когда́ мы говори'м о челове'ке, э'то зна'чит, что он занима'ется каки'м-то де'лом, что'бы получи'ть результа'т, часто за де'ньги и́ли для до́стижения це'ли. Когда́ мы говори'м о вещи', э'то зна'чит, что она́ функциони'рует и́ли де'йствует пра'вильно.\n" +
                "Э'то сло'во про́исходит от ста'рого славя'нского ко'рня, кото'рый был свя'зан с поня'тием труда́ и́ли слу'жбы. В ру'сском языке' э'то сло'во существу'ет о'чень давно' и всегда́ обознача'ло де'ятельность и́ли труд. О'но не бы'ло за́имствовано из други'х языко'в, а разви'лось в самом ру'сском языке'.\n" +
                "Э'то сло'во мо'жно испо'льзовать в ра'зных ситуа'циях: и в официа'льной, и в неофициа'льной ре'чи. Мы говори'м его́ ка'ждый день, когда́ расска'зываем о свое'й профе'ссии, о том, что де'лает како'й-то механи'зм, и́ли когда́ описы'ваем любу'ю де'ятельность. Э'то сло'во одина'ково ча'сто встреча'ется и в письме'нной, и в у'стной ре'чи, и во всех регио'нах, где говоря'т по-ру'сски.\n" +
                "Сло'во состои'т из ко'рня \"работ-\", кото'рый несёт основно'е значе'ние труда́ и́ли де'ятельности, и глаго'льного суффи'кса \"-а-\", кото'рый помога'ет образова'ть глаго'л. Оконча'ние \"-ть\" показы'вает, что э'то неопределённая фо'рма глаго'ла. От э'того же ко'рня образу'ются други'е слова', наприме'р: рабо'та, рабо'тник, рабо'тодатель.";

        String wordInformationWithOddErrors = "Помеще́ние внутри́ зда́ния, огра́ниченное стена́ми, предназна́ченное для жилья́, рабо́ты или отды́ха челове́ка. Обы́чно име́ет окна́, две́рь и мо́жет быть ча́стью кварти́ры, до́ма или офи́са.\n" +
                "\n" +
                "Сло́во произошло́ из старосла́вянского языка́, где \"комната\" бы́ла свя́зана со слова́ми, обознача́ющими за́мкнутое простра́нство. Перво́начально упо́треблялось в зна́ти и дворя́нских семья́х.\n" +
                "\n" +
                "Упо́требляется в повседне́вной ре́чи, в быту́, при описа́нии жили́ща. Испо́льзуется во всех стил́ях речи́: быто́вом, офици́альном, разгово́рном. Часто́ встреча́ется в расска́зах о до́ме, в объявле́ниях о не́движимости.\n" +
                "\n" +
                "Морфологи́чески сло́во состои́т из корня \"комн-\" и суффикса \"-ата\", кото́рый indicates su\u00ADbstan\u00ADtive no\u00ADmi\u00ADna\u00ADli\u00ADza\u00ADtion. Измене́ние по паде́жам проис́ходит по типу существи́тельных же́нского ро́да.";

        String wordInformationForRoom = "Место' внутри' здания', предназначенное для жизни', работы' или отдыха'. Обычно' комната' имеет стены', пол, потолок и дверь, а также может быть оборудована мебелью и различными предметами в зависимости от её назначения'.\n" +
                "\n" +
                "Слово' происходит из старославя'нского языка', где корень «ком-» связан со словом «камера» и первоначально' означал закрытое простра'нство. В совреме'нном русском языке' это нейтра'льное бытовое' слово с широким употребле'нием.\n" +
                "\n" +
                "Использу'ется преимущественно в разгово'рной и письме'нной речи', в быту', в офисах, учебных заведе'ниях. Подходит для формальных и неформа'льных ситуа'ций, употребля'ется во всех региона'х России'.\n" +
                "\n" +
                "Морфологи'чески состоит из корня «ком-» и суффикса «-ната». Существительное' женского' рода с типичной для русского' языка структу'рой словообразова'ния.";

        Set<Definition> definitions = definitionGenerator.createShortDefinitions(shortDefinition, word);
        //WordInformation info = definitionGenerator.createWordInformation(wordInformation, word);
        //WordInformation roomInfo = definitionGenerator.createWordInformation(wordInformationForRoom, room);
        Set<Definition> roomDefinitions = definitionGenerator.createShortDefinitions(shortDefinition, room);

        Assertions.assertAll(
                () -> Assertions.assertEquals(3, definitions.size()),
                () -> Assertions.assertEquals(3, roomDefinitions.size())
                //() -> Assertions.assertNotNull(roomInfo)
        );
    }
}
