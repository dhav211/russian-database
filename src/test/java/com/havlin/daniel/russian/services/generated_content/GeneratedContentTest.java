package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.generated_content.GeneratedContentError;
import com.havlin.daniel.russian.entities.generated_content.ReadingLevel;
import com.havlin.daniel.russian.entities.generated_content.Sentence;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import com.havlin.daniel.russian.repositories.generated_content.DefinitionRepository;
import com.havlin.daniel.russian.repositories.generated_content.GeneratedContentErrorRepository;
import com.havlin.daniel.russian.repositories.generated_content.SentenceRepository;
import com.havlin.daniel.russian.services.dictionary.SentenceService;
import com.havlin.daniel.russian.services.dictionary.WordService;
import com.havlin.daniel.russian.services.retrieval.WordRetrievalService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class GeneratedContentTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private GeneratedContentService generatedContentService;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private WordService wordService;

    @Autowired
    private GeneratedContentErrorService generatedContentErrorService;

    @Autowired
    private GeneratedContentErrorRepository generatedContentErrorRepository;

    @Autowired
    private SentenceRepository sentenceRepository;

    @Autowired
    private DefinitionRepository definitionRepository;

    @Autowired
    private WordRetrievalService wordRetrievalService;

    @Autowired
    private SentenceService sentenceService;

    @Test
    @Transactional
    public void saveGeneratedContentToDB() {
        String beginnerSentences = "Мы ча'сто говори'м о хоро'ших кни'гах.\n" +
                "We often talk about good books.\n" +
                "В э'том го'роде мно'го хоро'ших люде'й.\n" +
                "There are many good people in this city.\n" +
                "Я мечта'ю о хоро'ших путеше'ствиях по ми'ру.\n" +
                "I dream about good travels around the world.\n" +
                "\n" +
                "Де'ти игра'ют хоро'шими игру'шками во дворе'.\n" +
                "Children play with good toys in the yard.\n" +
                "С хоро'шими друзья'ми все'гда ве'село проводи'ть вре'мя.\n" +
                "It's always fun to spend time with good friends.\n" +
                "Он помога'ет други'м хоро'шими сове'тами и де'йствиями.\n" +
                "He helps others with good advice and actions.\n" +
                "\n" +
                "Мы ви'дели хоро'шие фи'льмы в кинотеа'тре вче'ра.\n" +
                "We saw good films at the cinema yesterday.\n" +
                "Купи'те, пожа'луйста, хоро'шие я'блоки для пирога'.\n" +
                "Please buy good apples for the pie.\n" +
                "Студе'нты чита'ют хоро'шие кни'ги в библиоте'ке.\n" +
                "Students read good books in the library.\n" +
                "\n" +
                "Я пригласи'л на ве'черинку хоро'ших друзе'й из университе'та.\n" +
                "I invited good friends from the university to the party.\n" +
                "Она' зна'ет мно'гих хоро'ших специали'стов в свое'й о'бласти.\n" +
                "She knows many good specialists in her field.\n" +
                "Мы ча'сто встреча'ем хоро'ших люде'й в э'том ма'леньком го'роде.\n" +
                "We often meet good people in this small town.\n" +
                "\n" +
                "Он все'гда ра'д хоро'шим новостя'м с рабо'ты.\n" +
                "He is always happy about good news from work.\n" +
                "Я жела'ю хоро'шим студе'нтам успе'ха на экза'менах.\n" +
                "I wish good students success in their exams.\n" +
                "Спаси'бо хоро'шим сосе'дям за по'мощь с переез'дом.\n" +
                "Thanks to good neighbors for help with the move.\n" +
                "\n" +
                "У нас нет хоро'ших пла'нов на за'втра'шний день.\n" +
                "We don't have good plans for tomorrow.\n" +
                "Я скуча'ю по хоро'ших дням, когда' мы бы'ли вме'сте.\n" +
                "I miss the good old days when we were together.\n" +
                "Э'то исто'рия о хоро'ших дела'х, кото'рые измени'ли мир.\n" +
                "This is a story about good deeds that changed the world.\n" +
                "\n" +
                "Хоро'шие друзья' все'гда подде'рживают друг дру'га в тру'дные моме'нты.\n" +
                "Good friends always support each other in difficult moments.\n" +
                "Хоро'шие о'тзывы о продук'те ва'жны для прода'ж.\n" +
                "Good reviews about a product are important for sales.\n" +
                "Э'ти хоро'шие кроссо'вки о'чень удо'бные для бе'га.\n" +
                "These good sneakers are very comfortable for running.\n" +
                "\n" +
                "О'н расска'зывал мне о хоро'шем рестора'не в це'нтре го'рода.\n" +
                "He was telling me about a good restaurant in the city center.\n" +
                "Я ду'мал о хоро'шем сове'те, кото'рый ты мне дал.\n" +
                "I was thinking about the good advice you gave me.\n" +
                "В хоро'шем до'ме всегда' те'пло и уют'но зи'мними вечера'ми.\n" +
                "In a good home it's always warm and cozy on winter evenings.\n" +
                "\n" +
                "С хоро'шим ножо'м ле'гко ре'зать хлеб и овощи'.\n" +
                "It's easy to cut bread and vegetables with a good knife.\n" +
                "Э'тот сту'л сде'лан хоро'шим мастеро'м из де'рева.\n" +
                "This chair was made by a good master from wood.\n" +
                "С хоро'шим компью'тером рабо'та идёт быстре'е и эффе'ктивнее.\n" +
                "Work goes faster and more efficiently with a good computer.\n" +
                "\n" +
                "Мы купи'ли хоро'шее вино' для пра'здничного стола'.\n" +
                "We bought good wine for the holiday table.\n" +
                "Он сказа'л мне что'-то хоро'шее, и я улыбну'лась.\n" +
                "He told me something good, and I smiled.\n" +
                "Я ви'жу хоро'шее бу'дущее для на'шей компа'нии.\n" +
                "I see a good future for our company.\n" +
                "\n" +
                "Я да'л пода'рок хоро'шему ма'льчику на день рожде'ния.\n" +
                "I gave a present to a good boy for his birthday.\n" +
                "По хоро'шему ко'нкурсу, он побе'дил в соревнова'нии.\n" +
                "By a good competition, he won the contest.\n" +
                "Мы обяза'ны хоро'шему руково'дителю за успе'х на'шего прое'кта.\n" +
                "We owe the success of our project to a good leader.\n" +
                "\n" +
                "По'сле хоро'шего сна я чу'вствую себя' бо'дро и гото'в к рабо'те.\n" +
                "After a good sleep, I feel refreshed and ready for work.\n" +
                "Я не ви'дел хоро'шего врача' в э'той кли'нике.\n" +
                "I didn't see a good doctor in this clinic.\n" +
                "Цена' хоро'шего образова'ния высока', но э'то того' сто'ит.\n" +
                "The price of a good education is high, but it's worth it.\n" +
                "\n" +
                "Э'то хоро'шее реше'ние для на'шей пробле'мы.\n" +
                "This is a good solution for our problem.\n" +
                "За'втра бу'дет хоро'шее вре'мя для прогу'лки в па'рке.\n" +
                "Tomorrow will be a good time for a walk in the park.\n" +
                "Са'мое хоро'шее уже' произошло', и мы счастли'вы.\n" +
                "The best has already happened, and we are happy.\n" +
                "\n" +
                "Мы говори'ли о хоро'шей иде'е для но'вого старта'па.\n" +
                "We talked about a good idea for a new startup.\n" +
                "В хоро'шей по'годе прия'тно гуля'ть по бе'регу мо'ря.\n" +
                "It's pleasant to walk along the seashore in good weather.\n" +
                "Он мечта'ет о хоро'шей рабо'те с высоко'й за'работной пла'той.\n" +
                "He dreams about a good job with a high salary.\n" +
                "\n" +
                "Она' облада'ет хоро'шей па'мятью на и'мена и да'ты.\n" +
                "She possesses a good memory for names and dates.\n" +
                "С хоро'шей кни'гой мо'жно забы'ть о пробле'мах на не'сколько часо'в.\n" +
                "With a good book, one can forget about problems for several hours.\n" +
                "Он го'рдится свое'й хоро'шей успева'емостью в шко'ле.\n" +
                "He is proud of his good academic performance at school.\n" +
                "\n" +
                "С хоро'шею подру'гой все'гда найдётся о чём поговори'ть.\n" +
                "With a good friend there's always something to talk about.\n" +
                "Она' любу'ется хоро'шею карти'ной, вися'щей на стене'.\n" +
                "She admires a good painting hanging on the wall.\n" +
                "Я наслажда'юсь хоро'шею музы'кой, когда' е'ду в маши'не.\n" +
                "I enjoy good music when I drive in the car.\n" +
                "\n" +
                "Я ви'дел хоро'шую де'вушку на у'лице и улыбну'лся ей.\n" +
                "I saw a good girl on the street and smiled at her.\n" +
                "Она' купи'ла хоро'шую су'мку в но'вом мага'зине.\n" +
                "She bought a good bag at the new store.\n" +
                "Мы и'щем хоро'шую кварти'ру для наше'й семьи'.\n" +
                "We are looking for a good apartment for our family.\n" +
                "\n" +
                "Я жела'ю тебе' хоро'шей по'годы на отды'хе.\n" +
                "I wish you good weather for your vacation.\n" +
                "По хоро'шей доро'ге е'хать быстре'е и безопа'снее.\n" +
                "It's faster and safer to drive on a good road.\n" +
                "Мы шли к хоро'шей це'ли, прео'долевая все' тру'дности.\n" +
                "We were going towards a good goal, overcoming all difficulties.\n" +
                "\n" +
                "У нас нет хоро'шей иде'и для пода'рка ма'ме.\n" +
                "We don't have a good idea for a gift for mom.\n" +
                "Я хочу' кусо'к хоро'шей пи'ццы на у'жин.\n" +
                "I want a piece of good pizza for dinner.\n" +
                "Он ждёт хоро'шей ново'сти о своём собесе'довании.\n" +
                "He is waiting for good news about his interview.\n" +
                "\n" +
                "Э'та хоро'шая кни'га мне нра'вится и я её рекоменду'ю.\n" +
                "I like this good book and I recommend it.\n" +
                "Моя' хоро'шая подру'га прие'дет за'втра из друго'го го'рода.\n" +
                "My good friend will arrive tomorrow from another city.\n" +
                "Сего'дня была' хоро'шая по'года для прогу'лки на вели'ке.\n" +
                "Today was good weather for a bike ride.\n" +
                "\n" +
                "О'н расска'зывал мне о хоро'шем дру'ге из свое'го де'тства.\n" +
                "He was telling me about a good friend from his childhood.\n" +
                "В хоро'шем мага'зине мно'го покупа'телей да'же в бу'дние дни.\n" +
                "There are many customers in a good store even on weekdays.\n" +
                "Он всегда' мечта'ет о хоро'шем у'тре с ча'шкой ко'фе.\n" +
                "He always dreams about a good morning with a cup of coffee.\n" +
                "\n" +
                "Я пообе'дал с хоро'шим аппети'том по'сле долго'й прогу'лки.\n" +
                "I had lunch with a good appetite after a long walk.\n" +
                "С хоро'шим учи'телем лю'бой уро'к интере'сен и познавате'лен.\n" +
                "With a good teacher, any lesson is interesting and informative.\n" +
                "Он де'лится хоро'шим настрое'нием со все'ми вокру'г.\n" +
                "He shares his good mood with everyone around.\n" +
                "\n" +
                "Я прочита'л хоро'ший рома'н за вы'ходные дни.\n" +
                "I read a good novel over the weekend.\n" +
                "Мы ви'дели хоро'ший фи'льм, кото'рый нас впечатли'л.\n" +
                "We saw a good film that impressed us.\n" +
                "Он купи'л хоро'ший пода'рок свое'й сестре' на Рождество'.\n" +
                "He bought a good present for his sister for Christmas.\n" +
                "\n" +
                "Мы жда'ли хоро'шего тре'нера для на'шей футбо'льной кома'нды.\n" +
                "We were waiting for a good coach for our soccer team.\n" +
                "Кто' ви'дел хоро'шего па'рня, кото'рый помога'ет пожилы'м?\n" +
                "Who saw a good guy who helps the elderly?\n" +
                "Я хочу' пригласи'ть хоро'шего музыкан'та на наш пра'здник.\n" +
                "I want to invite a good musician to our celebration.\n" +
                "\n" +
                "Мы пошли' к хоро'шему специали'сту за сове'том.\n" +
                "We went to a good specialist for advice.\n" +
                "Я рассказа'л новость хоро'шему сосе'ду, и он обра'довался.\n" +
                "I told the news to a good neighbor, and he was happy.\n" +
                "О'н подошёл к хоро'шему мо'лодому челове'ку, что'бы спроси'ть доро'гу.\n" +
                "He approached a good young man to ask for directions.\n" +
                "\n" +
                "По'сле хоро'шего сна я чу'вствую себя' бо'дро.\n" +
                "After a good sleep, I feel refreshed.\n" +
                "Я не ви'дел хоро'шего врача' в э'той кли'нике.\n" +
                "I didn't see a good doctor in this clinic.\n" +
                "Цена' хоро'шего образова'ния высока'.\n" +
                "The price of a good education is high.\n" +
                "\n" +
                "Э'тот хоро'ший дом о'чень ста'рый, но кре'пкий.\n" +
                "This good house is very old, but sturdy.\n" +
                "Мой но'вый сосе'д - о'чень хоро'ший челове'к.\n" +
                "My new neighbor is a very good person.\n" +
                "Э'то был хоро'ший день для рыба'лки на о'зере.\n" +
                "It was a good day for fishing on the lake.\n" +
                "\n" +
                "Де'ти сего'дня о'чень хороши' и послу'шны.\n" +
                "The children are very good and obedient today.\n" +
                "Э'ти кни'ги де'йствительно хороши', я их всем рекоменду'ю.\n" +
                "These books are really good, I recommend them to everyone.\n" +
                "Все' его' и'деи были' хороши', но не'которые нереализу'емы.\n" +
                "All his ideas were good, but some are not feasible.\n" +
                "\n" +
                "Сего'дня я чу'вствую себя' хорошо' по'сле отпу'ска.\n" +
                "Today I feel good after vacation.\n" +
                "У нас всё хорошо', не волну'йтесь за нас.\n" +
                "Everything is good with us, don't worry about us.\n" +
                "Э'тот ко'фе па'хнет о'чень хорошо', как у'тром в де'ревне.\n" +
                "This coffee smells very good, like in the village in the morning.\n" +
                "\n" +
                "Э'та де'вушка о'чень хороша' и у'мна.\n" +
                "This girl is very good and smart.\n" +
                "Пого'да сего'дня просто' хороша' для прогу'лки.\n" +
                "The weather today is simply good for a walk.\n" +
                "Ста'рая пе'сня по'-пре'жнему хороша', она' навева'ет воспо'минания.\n" +
                "The old song is still good, it brings back memories.\n" +
                "\n" +
                "Он хоро'ш в свое'й рабо'те, все'гда вы'полняет её отли'чно.\n" +
                "He is good at his job, always performs it excellently.\n" +
                "Э'тот фильм о'чень хоро'ш, я смотре'л его' два'жды.\n" +
                "This film is very good, I watched it twice.\n" +
                "Мой но'вый сосе'д, хотя' и ста'рый, но хоро'ш со'бой.\n" +
                "My new neighbor, though old, is good-looking.\n" +
                "\n" +
                "Он мой лу'чший друг со шко'льных лет.\n" +
                "He is my best friend since school years.\n" +
                "Э'тот рестора'н предлага'ет лу'чший се'рвис в го'роде.\n" +
                "This restaurant offers the best service in the city.\n" +
                "Я все'гда стремлю'сь к лу'чшему результа'ту в любо'м де'ле.\n" +
                "I always strive for the best result in any matter.\n" +
                "\n" +
                "Э'тот вариа'нт наилу'чший из всех', что мы рассмотре'ли.\n" +
                "This option is the very best of all that we considered.\n" +
                "Жела'ю вам наилу'чшего успе'ха в учёбе и жи'зни.\n" +
                "I wish you the very best success in your studies and life.\n" +
                "Мы вы'брали наилу'чший спосо'б реше'ния пробле'мы.\n" +
                "We chose the very best way to solve the problem.\n" +
                "\n" +
                "Сего'дня пого'да лу'чше, чем вчера', мо'жно пойти' гуля'ть.\n" +
                "Today the weather is better than yesterday, we can go for a walk.\n" +
                "Ты мо'жешь сде'лать э'то лу'чше, я ве'рю в тебя'.\n" +
                "You can do it better, I believe in you.\n" +
                "Мне лу'чше уйти' сейча'с, я уста'л.\n" +
                "It's better for me to leave now, I'm tired.";

        String intermediateSentences = "Мы' ча'сто вспомина'ем о хоро'ших моментах из на'шего путеше'ствия.\n" +
                "We often recall the good moments from our trip.\n" +
                "В хоро'ших компа'ниях всегда' ца'рит атмосфе'ра взаимопо'нимания и дове'рия.\n" +
                "In good companies, an atmosphere of mutual understanding and trust always prevails.\n" +
                "Руководи'тель подели'лся со'трудникам пла'нами о хоро'ших перспекти'вах развития' прое'кта.\n" +
                "The manager shared with employees plans about good prospects for project development.\n" +
                "\n" +
                "То'лько хоро'шими дела'ми мо'жно доказа'ть свою' пре'данность идеа'лам гума'нности.\n" +
                "Only with good deeds can one prove their devotion to the ideals of humanity.\n" +
                "Благодаря' хоро'шими организато'рами, конфере'нция прошла' без сучка' и задо'ринки.\n" +
                "Thanks to good organizers, the conference went off without a hitch.\n" +
                "Мы' наслажда'лись хоро'шими собесе'дниками весь ве'чер, обсу'ждая филосо'фские пробле'мы.\n" +
                "We enjoyed good conversationalists all evening, discussing philosophical problems.\n" +
                "\n" +
                "Я' всегда' ищу' хоро'шие кни'ги, что'бы расши'рить свой кругозо'р и лекси'кон.\n" +
                "I always look for good books to broaden my horizons and vocabulary.\n" +
                "По'сле до'лгих по'исков мы' наконец-то нашли' хоро'шие биле'ты на конце'рт наше'й люби'мой гру'ппы.\n" +
                "After a long search, we finally found good tickets for our favorite band's concert.\n" +
                "Не забыва'й смотре'ть хоро'шие фи'льмы, они' помога'ют рассла'биться и поду'мать о ва'жном.\n" +
                "Don't forget to watch good films; they help you relax and think about important things.\n" +
                "\n" +
                "Пре'жде чем начина'ть но'вый би'знес, на'до набра'ть хоро'ших специали'стов.\n" +
                "Before starting a new business, one needs to recruit good specialists.\n" +
                "На'м удало'сь пригласи'ть хоро'ших друзе'й на на'шу вечери'нку по случа'ю новосе'лья.\n" +
                "We managed to invite good friends to our housewarming party.\n" +
                "Тре'нер обеща'л подгото'вить хоро'ших спортсме'нов к предстоя'щим олимпи'йским и'грам.\n" +
                "The coach promised to prepare good athletes for the upcoming Olympic Games.\n" +
                "\n" +
                "Мы' стреми'мся к хоро'шим результа'там в ка'ждом прое'кте, кото'рый мы' начина'ем.\n" +
                "We strive for good results in every project we start.\n" +
                "Спаси'бо всем хоро'шим лю'дям, кото'рые оказа'ли на'м бесце'нную по'мощь в тру'дную мину'ту.\n" +
                "Thanks to all good people who provided us invaluable help in a difficult moment.\n" +
                "По'сле до'лгих деба'тов, коали'ция пришла' к хоро'шим соглаше'ниям по ключе'вым вопроса'м.\n" +
                "After long debates, the coalition came to good agreements on key issues.\n" +
                "\n" +
                "К сожале'нию, по'сле перегово'ров у них' не оста'лось хоро'ших воспомина'ний о той встре'че.\n" +
                "Unfortunately, after the negotiations, they had no good memories of that meeting left.\n" +
                "В э'том го'роде, ка'жется, совсе'м нет хоро'ших рестора'нов с национа'льной ку'хней.\n" +
                "In this city, it seems, there are no good restaurants with national cuisine at all.\n" +
                "Без хоро'ших инструме'нтов невозмо'жно выполня'ть сло'жную рабо'ту каче'ственно.\n" +
                "Without good tools, it's impossible to perform complex work with high quality.\n" +
                "\n" +
                "Хоро'шие дру'зья – э'то бесце'нный да'р, кото'рый на'до цени'ть и бере'чь.\n" +
                "Good friends are a priceless gift that must be cherished and protected.\n" +
                "Хоро'шие ново'сти разно'сятся бы'стро, вдохновля'я всех на но'вые сверше'ния.\n" +
                "Good news spreads quickly, inspiring everyone to new achievements.\n" +
                "Эти хоро'шие иде'и заслужива'ют того', что'бы их воплоти'ли в жизнь.\n" +
                "These good ideas deserve to be brought to life.\n" +
                "\n" +
                "Мечта'я о хоро'шем бу'дущем, не забыва'й де'йствовать в настоя'щем.\n" +
                "Dreaming of a good future, don't forget to act in the present.\n" +
                "В хоро'шем настрое'нии вся'ка рабо'та спо'рится быстре'е и эффе'ктивнее.\n" +
                "In a good mood, any work proceeds faster and more efficiently.\n" +
                "На фестива'ле я' при'нял уча'стие в хоро'шем семина'ре по цифро'вому иску'сству.\n" +
                "At the festival, I participated in a good seminar on digital art.\n" +
                "\n" +
                "Не'которые лю'ди счита'ют, что мо'жно ста'ть хоро'шим специа'листом, то'лько усер'дно рабо'тая.\n" +
                "Some people believe that one can become a good specialist only by working hard.\n" +
                "Э'то'т челове'к стал хоро'шим приме'ром для подража'ния для мно'гих молоды'х уче'ных.\n" +
                "This person became a good role model for many young scientists.\n" +
                "По'сле долго'го перелё'та, он наслажда'лся горя'чим ча'ем с хоро'шим ме'дом.\n" +
                "After a long flight, he enjoyed hot tea with good honey.\n" +
                "\n" +
                "На'ша зада'ча – сде'лать что'-то де'йствительно хоро'шее для всего' о'бщества.\n" +
                "Our task is to do something truly good for the entire society.\n" +
                "Он всегда' и'щет хоро'шее во всём, да'же в са'мых сло'жных ситуа'циях.\n" +
                "He always looks for the good in everything, even in the most difficult situations.\n" +
                "Мы' купи'ли о'чень хоро'шее пальто' по ски'дке, кото'рое прослу'жит мно'го лет.\n" +
                "We bought a very good coat on sale that will last many years.\n" +
                "\n" +
                "К хоро'шему врачу' всегда' стои'т дли'нная очере'дь из пациен'тов.\n" +
                "There is always a long line of patients for a good doctor.\n" +
                "Я' стреми'лся к хоро'шему результа'ту, и э'то' мотиви'ровало меня' к дальне'йшим де'йствиям.\n" +
                "I strived for a good result, and this motivated me to further actions.\n" +
                "По'сле тяже'лой неде'ли, я' при'вык потака'ть себе' чему'-то хоро'шему.\n" +
                "After a tough week, I've gotten used to treating myself to something good.\n" +
                "\n" +
                "У них' не' было ничего' хоро'шего, что'бы предложи'ть на'м в то'т моме'нт.\n" +
                "They had nothing good to offer us at that moment.\n" +
                "По'сле тако'го до'лгого пути' у меня' нет хоро'шего настрое'ния шути'ть.\n" +
                "After such a long journey, I am not in a good mood to joke.\n" +
                "Для достиже'ния тако'го успе'ха не'обходимо мно'го хоро'шего терпе'ния и упо'рства.\n" +
                "To achieve such success, a lot of good patience and perseverance are necessary.\n" +
                "\n" +
                "Са'мое хоро'шее в э'той ситуа'ции то', что все' оста'лись жи'вы и здоро'вы.\n" +
                "The best thing in this situation is that everyone remained alive and well.\n" +
                "Э'то' хоро'шее реше'ние, кото'рое уче'сть интере'сы всех уча'стников прое'кта.\n" +
                "This is a good solution that takes into account the interests of all project participants.\n" +
                "Хоро'шее воспита'ние проявля'ется в уме'нии уважа'ть чужо'е мне'ние.\n" +
                "Good upbringing is manifested in the ability to respect others' opinions.\n" +
                "\n" +
                "Я' всегда' мечта'л о хоро'шей рабо'те с досто'йной за'работной пла'той.\n" +
                "I always dreamed of a good job with a decent salary.\n" +
                "В хоро'шей компа'нии вре'мя лети'т незаме'тно, а бесе'ды прохо'дят оживлённо.\n" +
                "In good company, time flies by unnoticed, and conversations are lively.\n" +
                "Он ча'сто вспомина'ет о хоро'шей подру'ге свое'го де'тства, с кото'рой потеря'л связь.\n" +
                "He often remembers a good friend from his childhood with whom he lost touch.\n" +
                "\n" +
                "Она' писа'ла стихи' хоро'шей ру'чкой, кото'рую подари'ла ей' ба'бушка.\n" +
                "She wrote poems with a good pen that her grandmother gave her.\n" +
                "Мы' смо'жем добиться' успе'ха то'лько хоро'шей команд'ной рабо'той.\n" +
                "We can only achieve success through good teamwork.\n" +
                "Моя' сестра' ста'ла хоро'шей пиани'сткой благодаря' мно'голетним трениро'вкам.\n" +
                "My sister became a good pianist thanks to many years of training.\n" +
                "\n" +
                "Её' ли'цо сия'ло под хоро'шею шля'пою, не'смотря на паля'щее со'лнце.\n" +
                "Her face shone under a good hat, despite the scorching sun.\n" +
                "Дере'вня была' окружена' хоро'шею, плодо'родною зе'млею, бо'гатою урожа'ем.\n" +
                "The village was surrounded by good, fertile land, rich in harvest.\n" +
                "Её' му'зыка прони'зана хоро'шею, искре'ннею душо'ю.\n" +
                "Her music is imbued with a good, sincere soul.\n" +
                "\n" +
                "Мне' на'до купи'ть хоро'шую ка'меру для мое'го хо'бби – фотогра'фии приро'ды.\n" +
                "I need to buy a good camera for my hobby – nature photography.\n" +
                "Она' мечта'ет постро'ить хоро'шую карье'ру в ме'ждународной компа'нии.\n" +
                "She dreams of building a good career in an international company.\n" +
                "Мы' прочита'ли о'чень хоро'шую ста'тью о влия'нии кли'мата на мигра'цию птиц.\n" +
                "We read a very good article about the impact of climate on bird migration.\n" +
                "\n" +
                "Я' всегда' отношу'сь к хоро'шей по'годе как к по'воду для прогу'лки.\n" +
                "I always treat good weather as an occasion for a walk.\n" +
                "Ко'шка при'выкла к хоро'шей еде' и тепе'рь не е'ст дешёвый корм.\n" +
                "The cat got used to good food and now won't eat cheap cat food.\n" +
                "К хоро'шей иде'е всегда' прислу'шиваются, да'же е'сли она' исхо'дит от нео'пытного челове'ка.\n" +
                "A good idea is always listened to, even if it comes from an inexperienced person.\n" +
                "\n" +
                "Для тако'го сло'жного блю'да ну'жно мно'го хоро'шей и све'жей зе'лени.\n" +
                "For such a complex dish, a lot of good and fresh greens are needed.\n" +
                "Без хоро'шей подгото'вки к экза'менам не сто'ит ожида'ть высо'ких оце'нок.\n" +
                "Without good preparation for exams, one should not expect high grades.\n" +
                "У них' не' было хоро'шей причи'ны для отсро'чки платежа', и штра'фы бы'ли начисле'ны.\n" +
                "They had no good reason for delaying the payment, and fines were charged.\n" +
                "\n" +
                "На'ша сосе'дка – о'чень хоро'шая и отзы'вчивая же'нщина, всегда' гото'вая помо'чь.\n" +
                "Our neighbor is a very good and responsive woman, always ready to help.\n" +
                "Хоро'шая музы'ка спосо'бна подня'ть настрое'ние да'же в са'мый нена'стный де'нь.\n" +
                "Good music is capable of lifting one's spirits even on the most inclement day.\n" +
                "Э'то' была' о'чень хоро'шая возмо'жность по'высить свою' квалифика'цию и зна'ния.\n" +
                "This was a very good opportunity to improve one's qualifications and knowledge.\n" +
                "\n" +
                "В хоро'шем рестора'не вы' всегда' найдёте безупре'чное обслу'живание.\n" +
                "In a good restaurant, you will always find impeccable service.\n" +
                "Он расска'зывал о хоро'шем фи'льме, кото'рый по'смотрел на прошлой неде'ле.\n" +
                "He was talking about a good film he watched last week.\n" +
                "По'сле до'лгих разду'мий, мы' сошлись' на хоро'шем плане де'йствий.\n" +
                "After long deliberations, we agreed on a good plan of action.\n" +
                "\n" +
                "Он всегда' де'лится хоро'шим настрое'нием со свои'ми колле'гами и друзья'ми.\n" +
                "He always shares his good mood with his colleagues and friends.\n" +
                "Мы' добра'лись до горо'да хоро'шим авто'бусом, оснащённым всем необходи'мым.\n" +
                "We reached the city by a good bus equipped with everything necessary.\n" +
                "Что'бы быть хоро'шим учи'телем, на'до не то'лько зна'ть предме'т, но и уме'ть его' пода'ть.\n" +
                "To be a good teacher, one needs not only to know the subject but also to be able to present it.\n" +
                "\n" +
                "Я' хочу' купи'ть хоро'ший велосипе'д, что'бы е'здить на рабо'ту ка'ждый де'нь.\n" +
                "I want to buy a good bicycle to ride to work every day.\n" +
                "Да'же в плохо'й пого'де мо'жно уви'деть хоро'ший зака'т, е'сли присмотре'ться.\n" +
                "Even in bad weather, one can see a good sunset if one looks closely.\n" +
                "Мой брат' про'сит меня' посове'товать ему' хоро'ший компью'тер для и'гр.\n" +
                "My brother asks me to recommend him a good computer for gaming.\n" +
                "\n" +
                "Мы' ждём хоро'шего ветерина'ра, что'бы осмотре'ть на'шего пито'мца.\n" +
                "We are waiting for a good veterinarian to examine our pet.\n" +
                "На'м рекомендова'ли хоро'шего юри'ста, кото'рый специализи'руется на жили'щных вопроса'х.\n" +
                "We were recommended a good lawyer who specializes in housing issues.\n" +
                "По'сле уволь'нения ста'рого нача'льника, они' и'щут хоро'шего управля'ющего для депа'ртамента.\n" +
                "After the old boss was fired, they are looking for a good manager for the department.\n" +
                "\n" +
                "По хоро'шему счёту, он дол'жен был дав'но извини'ться за своё' поведе'ние.\n" +
                "By all accounts, he should have apologized for his behavior long ago.\n" +
                "Не стоит' торопи'ться с вы'водами, приди'те к хоро'шему мне'нию по'сле все'х фак'тов.\n" +
                "Don't rush to conclusions; come to a good opinion after all the facts.\n" +
                "Я' помогу' любо'му хоро'шему челове'ку, оказа'вшемуся в беде'.\n" +
                "I will help any good person who finds themselves in trouble.\n" +
                "\n" +
                "У меня' нет никако'го хоро'шего предчу'вствия о пред'стоящей встре'че.\n" +
                "I have no good premonition about the upcoming meeting.\n" +
                "Для э'того прое'кта не'обходимо мно'го хоро'шего ко'фе, что'бы сохраня'ть бо'дрость.\n" +
                "For this project, a lot of good coffee is needed to stay alert.\n" +
                "Они' отказа'лись от хоро'шего предложе'ния, потому' что их' це'ли бы'ли ины'ми.\n" +
                "They refused a good offer because their goals were different.\n" +
                "\n" +
                "Ка'ждый но'вый де'нь – э'то хоро'ший шанс нача'ть что'-то с чистого листа'.\n" +
                "Every new day is a good chance to start something from scratch.\n" +
                "Мой со'сед – о'чень хоро'ший челове'к, всегда' гото'вый подде'ржать и вы'слушать.\n" +
                "My neighbor is a very good person, always ready to support and listen.\n" +
                "Э'то'т музе'й – хоро'ший приме'р того', как мо'жно сочета'ть исто'рию и совре'менность.\n" +
                "This museum is a good example of how history and modernity can be combined.\n" +
                "\n" +
                "Все' э'ти предложе'ния хороши', но нам' ну'жно вы'брать са'мое эффе'ктивное.\n" +
                "All these proposals are good, but we need to choose the most effective one.\n" +
                "Его' шу'тки всегда' хороши' и помога'ют разряди'ть напряжённую обстано'вку.\n" +
                "His jokes are always good and help defuse a tense atmosphere.\n" +
                "Её' иде'и хороши', но им' не' хватает' практи'чности и конкре'тики.\n" +
                "Her ideas are good, but they lack practicality and specificity.\n" +
                "\n" +
                "Хорошо', что ты' на'конец-то приня'л пра'вильное реше'ние.\n" +
                "It's good that you finally made the right decision.\n" +
                "На'м всегда' бы'ло хорошо' вме'сте, несмо'тря на все' тру'дности.\n" +
                "We were always good together, despite all the difficulties.\n" +
                "Спа'ть на све'жем во'здухе о'чень хорошо' для здоро'вья.\n" +
                "Sleeping outdoors is very good for health.\n" +
                "\n" +
                "Э'то' пла'тье на тебе' о'чень хороша', подчёркивает фигу'ру.\n" +
                "This dress looks very good on you, emphasizing your figure.\n" +
                "Идея' хороша', но для её' реализа'ции потре'буется мно'го ресур'сов.\n" +
                "The idea is good, but its implementation will require many resources.\n" +
                "По'сле дождя', пого'да на у'лице была' ну' о'чень хороша', прямо' как на куро'рте.\n" +
                "After the rain, the weather outside was just very good, just like at a resort.\n" +
                "\n" +
                "Э'то'т план хоро'ш, но нам' ну'жно обсуди'ть дета'ли пе'ред нача'лом рабо'ты.\n" +
                "This plan is good, but we need to discuss the details before starting work.\n" +
                "Мой де'душка всегда' говори'л: \"До'ктор хоро'ш, да'же е'сли не' ле'чит, а то'лько слу'шает\".\n" +
                "My grandfather always used to say: \"A doctor is good, even if he doesn't treat but only listens.\"\n" +
                "Как же' э'то'т кофе' хоро'ш! Я' вы'пил уже' три' ча'шки.\n" +
                "How good this coffee is! I've already drunk three cups.\n" +
                "\n" +
                "Он счита'ется лу'чшим специа'листом в свое'й о'трасли благодаря' огро'мному о'пыту.\n" +
                "He is considered the best specialist in his field thanks to vast experience.\n" +
                "На'ша цель – предоста'вить клиен'там лу'чший се'рвис по са'мым конкуре'нтным це'нам.\n" +
                "Our goal is to provide clients with the best service at the most competitive prices.\n" +
                "Э'то'т фильм был призна'н лу'чшим детекти'вом го'да на ме'ждународном кинофестива'ле.\n" +
                "This film was recognized as the best detective of the year at the international film festival.\n" +
                "\n" +
                "Для достиже'ния наилу'чшего результа'та мы' должны' испо'льзовать все' име'ющиеся ресу'рсы.\n" +
                "To achieve the very best result, we must use all available resources.\n" +
                "У на'с есть наилу'чший ша'нс вы'играть э'ту' игру', е'сли мы' бу'дем игра'ть сла'женно.\n" +
                "We have the very best chance to win this game if we play cohesively.\n" +
                "На'м пообеща'ли наилу'чший се'рвис и индивидуа'льный подхо'д к ка'ждому клиен'ту.\n" +
                "We were promised the very best service and an individual approach to each client.\n" +
                "\n" +
                "Сегодня' на у'лице лу'чше, чем вчера', мо'жно вы'йти прогуля'ться в пар'ке.\n" +
                "It's better outside today than yesterday; one can go for a walk in the park.\n" +
                "Я' чу'вствую себя' значи'тельно лу'чше по'сле того', как по'спал десять' часо'в.\n" +
                "I feel significantly better after sleeping for ten hours.\n" +
                "Иногда' молча'ние лу'чше любы'х слов, осо'бенно в спо'ре.\n" +
                "Sometimes silence is better than any words, especially in an argument.";

        String definitions = "Э'то то, что отли'чно выпо'лняет свою' фу'нкцию и'ли о'чень поле'зно.\n" +
                "Что'-то, что прино'сит ра'дость и'ли удово'льствие челове'ку.\n" +
                "Челове'к, кото'рый ве'жливо себя' ведёт и де'лает до'брые дела'.";

        String longDefinition = "**хороший**\n" +
                "\n" +
                "Это слово используется, чтобы описать что-то или кого-то, что нам нравится и вызывает положительные эмоции. Обычно оно означает высокое качество, приятные свойства или хорошие качества характера.";
        String etymology = "Слово **\"хоро'ший\"** – одно из самых важных и старых в русском языке.\n" +
                "\n" +
                "1.  **Происхождение:** Оно имеет древние корни и пришло к нам из **древнерусского языка**, а его происхождение связано с **общеславянскими корнями**. Это означает, что подобные слова были и в других славянских языках.\n" +
                "\n" +
                "2.  **Корень и древнее значение:** Корень слова \"хороший\" – **\"хор-\"**. В древности этот корень был связан со значениями, которые касались **порядка, красоты, чего-то аккуратного, ограждённого или круга**.\n" +
                "\n" +
                "    *   Например, \"хор-\" мог означать \"огороженное место\" (как двор или участок земли), что-то \"правильное\", \"устроенное\" или \"красивое\".\n" +
                "\n" +
                "3.  **Развитие значения:**\n" +
                "    *   Изначально \"хороший\" могло означать \"красивый\", \"аккуратный\", \"сделанный по правилам\" или \"тот, что находится в порядке\".\n" +
                "    *   Постепенно значение слова расширилось. От идеи \"порядка\" и \"красоты\" оно перешло к более широкому смыслу \"добрый\", \"качественный\", \"приятный\", \"правильный\" – именно так мы используем его сегодня.\n" +
                "\n" +
                "4.  **Связанные слова:** От того же древнего корня \"хор-\" происходит, например, слово **\"хоромы\"** – это большой, красивый и хорошо построенный дом. Это показывает связь между \"хорошим\" и идеей чего-то \"красивого\", \"качественного\" и \"правильного\".";
        String usage = "Слово \"хороший\" – это очень частое и универсальное слово в русском языке.\n" +
                "\n" +
                "*   **Формальность или неформальность**: Это нейтральное слово. Вы можете использовать его как в формальных ситуациях (например, на работе или учёбе), так и в неформальных (с друзьями, в семье). Оно подходит для любого стиля общения.\n" +
                "\n" +
                "*   **Профессиональный или академический контекст**: Да, \"хороший\" очень часто используется в профессиональных и академических контекстах. Например, вы можете сказать: \"хороший результат\", \"хорошая идея\", \"хорошая оценка за экзамен\", \"хорошая работа\".\n" +
                "\n" +
                "*   **Письменный или устный язык**: Это слово одинаково часто встречается как в устной речи (мы говорим его каждый день), так и в письменной (в книгах, статьях, письмах, сообщениях).\n" +
                "\n" +
                "*   **Региональное использование**: Это стандартное слово русского языка. Его понимают и используют во всех регионах, где говорят по-русски. Нет никаких региональных особенностей.\n" +
                "\n" +
                "*   **Социальные ситуации**: Вы можете использовать \"хороший\", чтобы описывать почти всё:\n" +
                "    *   **Людей**: \"хороший человек\", \"хороший друг\", \"хороший учитель\".\n" +
                "    *   **Предметы**: \"хорошая книга\", \"хороший телефон\", \"хорошая машина\".\n" +
                "    *   **События**: \"хороший концерт\", \"хороший фильм\", \"хорошее путешествие\".\n" +
                "    *   **Погоду**: \"хорошая погода\".\n" +
                "    *   **Настроение**: \"хорошее настроение\".\n" +
                "    *   **Качество**: \"хорошее вино\", \"хорошая еда\".\n" +
                "    Оно выражает общую положительную оценку, одобрение или удовлетворение.\n" +
                "\n" +
                "*   **Культурные или стилистические особенности**: \"Хороший\" – это базовое прилагательное для выражения положительного качества. Оно простое, понятное и очень универсальное. Это одно из первых слов, которые учат в русском языке, чтобы сказать что-то позитивное.";
        String formation = "Слово \"хороший\" — это прилагательное. Давайте посмотрим, как оно построено:\n" +
                "\n" +
                "1.  **Корень (основа слова):** Главная часть слова — это **хорош-**. Именно она несёт основное значение \"good\" или \"добрый\".\n" +
                "2.  **Приставки (префиксы):** У этого слова нет приставок.\n" +
                "3.  **Суффиксы:** У этого слова нет суффиксов, которые бы создавали новое слово.\n" +
                "4.  **Окончание:** Часть слова **-ий** — это окончание прилагательного. Оно показывает, что слово \"хороший\" стоит в мужском роде, единственном числе и именительном падеже (или винительном для неодушевленных существительных).\n" +
                "\n" +
                "Таким образом, корень **хорош-** даёт значение, а окончание **-ий** указывает на грамматические характеристики (род, число, падеж) прилагательного. Вместе они образуют слово \"хороший\", которое мы используем для описания чего-то или кого-то \"доброго\" или \"качественного\" в мужском роде, например, \"хороший день\", \"хороший человек\".";
        Word word = wordRepository.findById(116L).get();

        InstantiatedGeneratedContentDTO generatedContentDTO = new InstantiatedGeneratedContentDTO();
        generatedContentDTO.sentences.put(ReadingLevel.BEGINNER, beginnerSentences);
        generatedContentDTO.sentences.put(ReadingLevel.INTERMEDIATE, intermediateSentences);
        generatedContentDTO.definitions = definitions;

        ApprovedGeneratedContent approvedGeneratedContent = generatedContentService.createCorrectedContentEntities(generatedContentDTO, word);
        wordService.saveGeneratedContentToWord(approvedGeneratedContent.sentences(), approvedGeneratedContent.definitions(), approvedGeneratedContent.words());

        Optional<Word> always = wordRepository.findById(160L);
        Set<Sentence> alwaysSentences = always.get().getSentences();

        Assertions.assertAll(
                () -> Assertions.assertTrue(alwaysSentences.size() > 1),
                () -> Assertions.assertTrue(alwaysSentences.stream().findFirst().get().getContainingWords().size() > 1)
        );
    }

    @Test
    @Transactional
    public void callGeminiToCreateSentences() {
        Optional<Word> retrievedWord = wordRetrievalService.getWordByIdForSentenceCreation(630L);

        if (retrievedWord.isPresent()) {
            Word word = retrievedWord.get();

            if (word.getSentences().isEmpty()) {
                generatedContentService.generateContentForWord(word, AiModel.GEMINI);
            }
        }

        Word savedWord = wordRepository.findById(114L).get();

        Assertions.assertAll(
                () -> Assertions.assertTrue(savedWord.getSentences().size() > 1),
                () -> Assertions.assertTrue(savedWord.getSentences()
                        .stream().findFirst().get().getContainingWords().size() > 1)
        );
    }

    @Test
    public void actuallyCreateSentences() {
        List<Long> ids = List.of(444L,
                457L,
                489L,
                521L,
                548L,
                639L,
                688L,
                729L,
                731L);
        for (Long id : ids) {
            Optional<Word> retrievedWord = wordRetrievalService.getWordByIdForSentenceCreation(id);

            if (retrievedWord.isPresent()) {
                Word word = retrievedWord.get();

                if (word.getSentences().size() < 65) {
                    generatedContentService.generateContentForWord(word, AiModel.GEMINI);
                }
            System.out.println("Finished generated content for word " + word.getBare() + " of id " + id);
            }

        }
    }

    @Test
    public void removeDuplicatesTest() {
        Set<Sentence> sentences = new HashSet<>();
        sentences.addAll(sentenceRepository.findAllByText("На'до быть преде'льно осторо'жным в разгово'ре с незнако'мыми людьми', осо'бенно в' больших города'х."));
        sentences.addAll(sentenceRepository.findAllByText("Доро'га вела' нас че'рез живопи'сные поля' и леса'."));

        int oldSentenceSize = sentences.size();

        sentences = generatedContentService.removeDuplicateSentences(sentences);

        Assertions.assertTrue(oldSentenceSize > sentences.size());
    }

    @Test
    @Transactional
    public void removeSentence() {
        long sentenceRepoSize = sentenceRepository.count();
        Sentence sentenceToRemove = sentenceRepository.findById(10222L).get();
        Sentence nullSentence = sentenceRepository.findById(342L).orElse(null);
        boolean isNullSentenceRemoved = sentenceService.removeSentence(nullSentence);
        sentenceService.removeSentence(sentenceToRemove);

        Assertions.assertAll(
                () -> Assertions.assertFalse(isNullSentenceRemoved)
        );
    }

    @Test
    @Transactional
    public void nothing() {
        sentenceService.findAndRemoveDuplicateSentences();
    }
}
