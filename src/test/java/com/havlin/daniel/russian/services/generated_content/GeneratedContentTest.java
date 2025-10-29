package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.generated_content.GeneratedContentError;
import com.havlin.daniel.russian.entities.generated_content.ReadingLevel;
import com.havlin.daniel.russian.entities.generated_content.Sentence;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import com.havlin.daniel.russian.repositories.generated_content.DefinitionRepository;
import com.havlin.daniel.russian.repositories.generated_content.GeneratedContentErrorRepository;
import com.havlin.daniel.russian.repositories.generated_content.SentenceRepository;
import com.havlin.daniel.russian.repositories.generated_content.WordInformationRepository;
import com.havlin.daniel.russian.services.dictionary.WordService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

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
    private WordInformationRepository wordInformationRepository;

    @Test
    @Transactional
    public void saveGeneratedContentToDB() {
        String definitions = "Продолжа'ть быть в како'м-ли'бо ме'сте и'ли состоя'нии.\n" +
                "Быть прису'тствующим по'сле того', как что'-то друго'е ушло' и'ли бы'ло забра'но.\n" +
                "Сохраня'ть своё' положе'ние и'ли состоя'ние в тече'ние како'го-ли'бо вре'мени.";

        String advancedSentences = "Ста'рый до'ктор, оста'вшийся на дежу'рстве в одино'честве, столкну'лся с необы'чным слу'чаем.\n" +
                "The old doctor, who remained on duty alone, encountered an unusual case.\n" +
                "Participle Active Past\n" +
                "\n" +
                "Её' сме'х, оста'вшийся в возду'хе, напомина'л о мину'вших днях и сбы'вшихся наде'ждах.\n" +
                "Her laughter, lingering in the air, reminded of days gone by and realized hopes.\n" +
                "Participle Active Past\n" +
                "\n" +
                "Ка'ждый, оста'вшийся без биле'та, до'лжен был сро'чно поки'нуть стадио'н.\n" +
                "Everyone who was left without a ticket had to immediately leave the stadium.\n" +
                "Participle Active Past\n" +
                "\n" +
                "Оста'вшись без словаря', студе'нту пришло'сь полага'ться на свою' интуи'цию и эруди'цию.\n" +
                "Having been left without a dictionary, the student had to rely on his intuition and erudition.\n" +
                "Gerund Past\n" +
                "\n" +
                "Мно'гие, оста'вшись наедине' со свои'ми мы'слями, на'чали переоце'нивать свои' приорите'ты.\n" +
                "Many, having been left alone with their thoughts, began to re-evaluate their priorities.\n" +
                "Gerund Past\n" +
                "\n" +
                "Худо'жник, оста'вшись на рассве'те на побере'жье, за'мер от великоле'пия зре'лища.\n" +
                "The artist, having remained on the shore at dawn, froze from the magnificence of the spectacle.\n" +
                "Gerund Past\n" +
                "\n" +
                "То'лько сильне'йшие ви'ды оста'нутся на э'той пла'нете, претерпе'в все климати'ческие измене'ния.\n" +
                "Only the strongest species will remain on this planet, having endured all climatic changes.\n" +
                "Third Person Plural\n" +
                "\n" +
                "Несмотря' на все предсказа'ния, не'которые тради'ции оста'нутся непоколеби'мыми в на'шем о'бществе.\n" +
                "Despite all predictions, some traditions will remain unyielding in our society.\n" +
                "Third Person Plural\n" +
                "\n" +
                "Если вы оста'нетесь дово'льны на'шим серви'сом, пожа'луйста, порекоменду'йте нас свои'м друзья'м.\n" +
                "If you remain satisfied with our service, please recommend us to your friends.\n" +
                "Second Person Plural\n" +
                "\n" +
                "Куда' вы оста'нетесь ночева'ть по'сле вечери'нки, е'сли го'родски'е оте'ли перепо'лнены?\n" +
                "Where will you stay overnight after the party if the city hotels are fully booked?\n" +
                "Second Person Plural\n" +
                "\n" +
                "Сегодня' мы оста'немся дома', что'бы пересмо'треть все сезо'ны люби'мого сериа'ла.\n" +
                "Today we will stay home to re-watch all seasons of our favorite series.\n" +
                "First Person Plural\n" +
                "\n" +
                "Дава'йте оста'немся ве'рными свои'м идеа'лам, несмо'тря на все искуше'ния и трудности.\n" +
                "Let's remain true to our ideals, despite all temptations and difficulties.\n" +
                "First Person Plural\n" +
                "\n" +
                "Я уве'рен, что' его' до'брое и'мя оста'нется в па'мяти люде'й на протяже'нии мно'гих поколе'ний.\n" +
                "I am sure that his good name will remain in people's memory for many generations.\n" +
                "Third Person Singular\n" +
                "\n" +
                "Что' бы ни случи'лось, и'стина всегда' оста'нется неизме'нной, как бы её' ни пыта'лись скры'ть.\n" +
                "Whatever happens, the truth will always remain unchanged, no matter how they try to hide it.\n" +
                "Third Person Singular\n" +
                "\n" +
                "Е'сли ты оста'нешься со мной' в э'ту тру'дную мину'ту, я никогда' э'того не забу'ду.\n" +
                "If you stay with me in this difficult moment, I will never forget it.\n" +
                "Second Person Singular\n" +
                "\n" +
                "Как ты оста'нешься споко'ен, когда' кру'гом цари'т ха'ос и неопределённость?\n" +
                "How will you remain calm when chaos and uncertainty reign all around?\n" +
                "Second Person Singular\n" +
                "\n" +
                "Я оста'нусь непоколеби'м в своём реше'нии, несмо'тря на все угро'зы и угово'ры.\n" +
                "I will remain unyielding in my decision, despite all threats and persuasion.\n" +
                "First Person Singular\n" +
                "\n" +
                "По'сле всего', что' произойдёт, я оста'нусь ве'рен свои'м принци'пам и убежде'ниям.\n" +
                "After everything that happens, I will remain true to my principles and beliefs.\n" +
                "First Person Singular\n" +
                "\n" +
                "В ста'ром до'ме оста'лись лишь э'хо про'шлых сме'хов и слёз, напоми'ная о его' исто'рии.\n" +
                "Only echoes of past laughter and tears remained in the old house, reminding of its history.\n" +
                "Past\n" +
                "\n" +
                "То'лько са'мые сто'йкие бойцы' оста'лись на поле' бо'я, отста'ивая после'дний рубе'ж.\n" +
                "Only the most resilient fighters remained on the battlefield, defending the last frontier.\n" +
                "Past\n" +
                "\n" +
                "Ка'жется, что' нам оста'лось то'лько ждать и наде'яться на благоприя'тный исхо'д собы'тий.\n" +
                "It seems that all that's left for us is to wait and hope for a favorable outcome.\n" +
                "Past\n" +
                "\n" +
                "По'сле того' как все ушли', на столе' оста'лось то'лько не'сколько кро'шек от пра'здничного пирога'.\n" +
                "After everyone left, only a few crumbs from the festive pie remained on the table.\n" +
                "Past\n" +
                "\n" +
                "Она' оста'лась прекра'сна, несмо'тря на проше'дшие годы' и пережи'тые испыта'ния.\n" +
                "She remained beautiful, despite the passing years and endured trials.\n" +
                "Past\n" +
                "\n" +
                "Эта' тради'ция оста'лась живу'чей, передава'ясь из поколе'ния в поколе'ние без измене'ний.\n" +
                "This tradition remained vital, passing from generation to generation without changes.\n" +
                "Past\n" +
                "\n" +
                "Он оста'лся равноду'шным к чужи'м страда'ниям, погружённый в свои' со'бственные мы'сли.\n" +
                "He remained indifferent to others' suffering, immersed in his own thoughts.\n" +
                "Past\n" +
                "\n" +
                "Несмотря' на предложе'ние уе'хать, он оста'лся сто'ять на перекрёстке, заду'мавшись о свое'й судьбе'.\n" +
                "Despite the offer to leave, he remained standing at the crossroads, pondering his fate.\n" +
                "Past\n" +
                "\n" +
                "Оста'ньтесь, пожа'луйста, на'шими гостя'ми ещё' на день, мы бу'дем о'чень ра'ды ва'шему прису'тствию.\n" +
                "Please, remain our guests for one more day; we will be very happy with your presence.\n" +
                "Imperative Plural\n" +
                "\n" +
                "Уважа'емые пассажи'ры, оста'ньтесь на свои'х места'х до по'лной остано'вки по'езда.\n" +
                "Dear passengers, please remain in your seats until the train comes to a complete stop.\n" +
                "Imperative Plural\n" +
                "\n" +
                "Оста'нься че'стным с самим' собо'й, и'стина всегда' освободи'т тебя' от бре'мени лжи.\n" +
                "Remain honest with yourself; the truth will always free you from the burden of lies.\n" +
                "Imperative Singular\n" +
                "\n" +
                "Молю', оста'нься со мной' в э'ту мра'чную но'чь, мне так ну'жна твоя' подде'ржка.\n" +
                "I beg you, stay with me on this dark night, I need your support so much.\n" +
                "Imperative Singular";

        String intermediateSentences = "Оста'вшийся хлеб разда'ли бе'дным.\n" +
                "The remaining bread was given to the poor.\n" +
                "Nominative\n" +
                "\n" +
                "Он не мог изба'виться от чу'вства гру'сти, оста'вшегося по'сле новосте'й.\n" +
                "He couldn't shake off the feeling of sadness that had remained after the news.\n" +
                "Genitive\n" +
                "\n" +
                "Мы обсуди'ли докуме'нты, оста'вшиеся от ста'рого архи'ва.\n" +
                "We discussed the documents that remained from the old archive.\n" +
                "Nominative\n" +
                "\n" +
                "Оста'вшись одна', она' начала' размышля'ть.\n" +
                "Having stayed alone, she began to reflect.\n" +
                "Gerund Past\n" +
                "\n" +
                "Оста'вшись ещё' на час, он наконе'ц у'ехал.\n" +
                "Having stayed for another hour, he finally left.\n" +
                "Gerund Past\n" +
                "\n" +
                "Оста'вшись в тени', шпио'н внима'тельно наблюда'л за свое'й це'лью.\n" +
                "Having remained in the shadows, the spy carefully observed his target.\n" +
                "Gerund Past\n" +
                "\n" +
                "Мно'гие вопро'сы оста'нутся без отве'та.\n" +
                "Many questions will remain unanswered.\n" +
                "Third Person Plural\n" +
                "\n" +
                "Го'сти оста'нутся до полу'ночи.\n" +
                "The guests will stay until midnight.\n" +
                "Third Person Plural\n" +
                "\n" +
                "Несмотря' на все уси'лия, следы' преступле'ния оста'нутся.\n" +
                "Despite all efforts, traces of the crime will remain.\n" +
                "Third Person Plural\n" +
                "\n" +
                "Вы оста'нетесь здесь на ночь?\n" +
                "Will you stay here for the night?\n" +
                "Second Person Plural\n" +
                "\n" +
                "Е'сли вы поспеши'те, то оста'нетесь незаме'ченными.\n" +
                "If you hurry, you will remain undetected.\n" +
                "Second Person Plural\n" +
                "\n" +
                "Долго ли вы оста'нетесь равноду'шными к э'тому де'лу?\n" +
                "How long will you remain indifferent to this matter?\n" +
                "Second Person Plural\n" +
                "\n" +
                "Мы оста'немся ве'рными на'шим при'нципам.\n" +
                "We will stay true to our principles.\n" +
                "First Person Plural\n" +
                "\n" +
                "Дава'йте оста'немся до'ма сего'дня ве'чером.\n" +
                "Let's stay home tonight.\n" +
                "First Person Plural\n" +
                "\n" +
                "Мы оста'немся еди'ными, несмотря' на все препя'тствия.\n" +
                "We will remain united despite all obstacles.\n" +
                "First Person Plural\n" +
                "\n" +
                "То'лько оди'н челове'к оста'нется.\n" +
                "Only one person will remain.\n" +
                "Third Person Singular\n" +
                "\n" +
                "Секре'т оста'нется ме'жду на'ми.\n" +
                "The secret will remain between us.\n" +
                "Third Person Singular\n" +
                "\n" +
                "Он оста'нется для меня' зага'дкой наве'ки.\n" +
                "He will remain an enigma to me forever.\n" +
                "Third Person Singular\n" +
                "\n" +
                "Ты оста'нешься со мной'?\n" +
                "Will you stay with me?\n" +
                "Second Person Singular\n" +
                "\n" +
                "Е'сли ты не бу'дешь боро'ться, то оста'нешься рабо'м свои'х стра'хов.\n" +
                "If you don't fight, you'll remain a slave to your fears.\n" +
                "Second Person Singular\n" +
                "\n" +
                "Ты оста'нешься в мое'й па'мяти наве'чно.\n" +
                "You will remain in my memory forever.\n" +
                "Second Person Singular\n" +
                "\n" +
                "Я оста'нусь в оте'ле на неде'лю.\n" +
                "I will stay at the hotel for a week.\n" +
                "First Person Singular\n" +
                "\n" +
                "Я оста'нусь оптими'стом, что бы ни случи'лось.\n" +
                "I will remain an optimist, no matter what happens.\n" +
                "First Person Singular\n" +
                "\n" +
                "Я оста'нусь пре'данным свои'м при'нципам.\n" +
                "I will remain loyal to my principles.\n" +
                "First Person Singular\n" +
                "\n" +
                "Они' оста'лись до'ма вчера'.\n" +
                "They stayed at home yesterday.\n" +
                "Past\n" +
                "\n" +
                "То'лько не'сколько моне'т оста'лись у него' в карма'не.\n" +
                "Only a few coins remained in his pocket.\n" +
                "Past\n" +
                "\n" +
                "Де'ти оста'лись восто'рженными по'сле представле'ния.\n" +
                "The children remained excited after the show.\n" +
                "Past\n" +
                "\n" +
                "Оста'лось все'го пять мину'т до конца' уро'ка.\n" +
                "Only five minutes remained until the end of the lesson.\n" +
                "Past\n" +
                "\n" +
                "Оста'лось не'ясным, почему' он у'ехал так внеза'пно.\n" +
                "It remained unclear why he left so suddenly.\n" +
                "Past\n" +
                "\n" +
                "Доста'точно еды' оста'лось на ещё' два дня.\n" +
                "Enough food remained for another two days.\n" +
                "Past\n" +
                "\n" +
                "Она' оста'лась помо'чь.\n" +
                "She stayed behind to help.\n" +
                "Past\n" +
                "\n" +
                "Ко'шка оста'лась одна' на кры'ше.\n" +
                "The cat remained alone on the roof.\n" +
                "Past\n" +
                "\n" +
                "Ма'ленькая наде'жда оста'лась в её' се'рдце.\n" +
                "A small hope remained in her heart.\n" +
                "Past\n" +
                "\n" +
                "То'лько оди'н солда'т оста'лся в живы'х.\n" +
                "Only one soldier remained alive.\n" +
                "Past\n" +
                "\n" +
                "Он оста'лся споко'йным, несмотря' на ха'ос.\n" +
                "He remained calm despite the chaos.\n" +
                "Past\n" +
                "\n" +
                "Портфе'ль оста'лся на сто'ле.\n" +
                "The briefcase remained on the table.\n" +
                "Past\n" +
                "\n" +
                "Пожа'луйста, оста'ньтесь ещё' ненадо'лго.\n" +
                "Please, stay a little longer.\n" +
                "Imperative Plural\n" +
                "\n" +
                "Оста'ньтесь бди'тельными!\n" +
                "Remain vigilant!\n" +
                "Imperative Plural\n" +
                "\n" +
                "Не оста'ньтесь равноду'шными пе'ред лицо'м несправедли'вости!\n" +
                "Don't remain indifferent in the face of injustice!\n" +
                "Imperative Plural\n" +
                "\n" +
                "Оста'нься со мной'!\n" +
                "Stay with me!\n" +
                "Imperative Singular\n" +
                "\n" +
                "Оста'нься си'льным, мой' друг.\n" +
                "Remain strong, my friend.\n" +
                "Imperative Singular\n" +
                "\n" +
                "Пожа'луйста, оста'нься на у'жин.\n" +
                "Please, stay for dinner.\n" +
                "Imperative Singular";

        String beginnerSentences = "Ма'льчeк, оста'вшийся оди'н, запла'кал.\n" +
                "The boy who remained alone started crying.\n" +
                "Short\n" +
                "\n" +
                "Мы нашли' после'дний кусо'к то'рта, оста'вшийся на сто'ле.\n" +
                "We found the last piece of cake, the one that remained on the table.\n" +
                "Prepositional\n" +
                "\n" +
                "Докуме'нт, оста'вшийся на столе', был о'чень ва'жным.\n" +
                "The document remaining on the table was very important.\n" +
                "Participle Active Past\n" +
                "\n" +
                "Оста'вшись до'ма, она' прочита'ла интере'сную кни'гу.\n" +
                "Having stayed at home, she read an interesting book.\n" +
                "Gerund Past\n" +
                "\n" +
                "Он помаха'л руко'й, оста'вшись на платфор'ме.\n" +
                "He waved goodbye, having remained on the platform.\n" +
                "Prepositional\n" +
                "\n" +
                "Оста'вшись голо'дным, кот гру'стно посмотре'л на хозя'ина.\n" +
                "Having remained hungry, the cat looked sadly at its owner.\n" +
                "Accusative\n" +
                "\n" +
                "Они' оста'нутся на ночь у нас.\n" +
                "They will stay for the night at our place.\n" +
                "Third Person Plural\n" +
                "\n" +
                "Е'сли не поспеши'те, то оста'нутся то'лько кро'шки.\n" +
                "If you don't hurry, only crumbs will remain.\n" +
                "Nominative\n" +
                "\n" +
                "Я наде'юсь, что де'ти оста'нутся дово'льны.\n" +
                "I hope the children will remain satisfied.\n" +
                "Short\n" +
                "\n" +
                "Вы оста'нетесь с на'ми или' пойдёте домо'й?\n" +
                "Will you stay with us or go home?\n" +
                "Second Person Plural\n" +
                "\n" +
                "Е'сли вы оста'нетесь здесь, то уви'дите всё са'ми.\n" +
                "If you stay here, you will see everything yourselves.\n" +
                "Nominative\n" +
                "\n" +
                "По'сле тако'го обе'да вы то'чно оста'нетесь сы'ты.\n" +
                "After such a dinner, you will definitely remain full.\n" +
                "Short\n" +
                "\n" +
                "Дава'йте оста'немся друзья'ми.\n" +
                "Let's remain friends.\n" +
                "First Person Plural\n" +
                "\n" +
                "Мы оста'немся в го'роде на выходны'е.\n" +
                "We will stay in the city for the weekend.\n" +
                "Prepositional\n" +
                "\n" +
                "Мы оста'немся здесь, пока' не начнётся дождь.\n" +
                "We will stay here until the rain starts.\n" +
                "Third Person Singular\n" +
                "\n" +
                "Он оста'нется рабо'тать в э'той компа'нии.\n" +
                "He will remain working in this company.\n" +
                "Third Person Singular\n" +
                "\n" +
                "Наде'юсь, что хоть что'-то от пирога' оста'нется.\n" +
                "I hope at least something from the pie will remain.\n" +
                "Genitive\n" +
                "\n" +
                "Она' оста'нется до'ма, е'сли будет плоха'я пого'да.\n" +
                "She will stay home if the weather is bad.\n" +
                "Nominative\n" +
                "\n" +
                "Ты оста'нешься со мно'й сего'дня ве'чером?\n" +
                "Will you stay with me tonight?\n" +
                "Second Person Singular\n" +
                "\n" +
                "Е'сли ты оста'нешься, мы смо'жем поговори'ть.\n" +
                "If you stay, we will be able to talk.\n" +
                "First Person Plural\n" +
                "\n" +
                "Ты оста'нешься без у'жина, е'сли не дое'шь суп.\n" +
                "You will remain without dinner if you don't finish your soup.\n" +
                "Genitive\n" +
                "\n" +
                "Я оста'нусь до'ма и почита'ю кни'гу.\n" +
                "I will stay home and read a book.\n" +
                "First Person Singular\n" +
                "\n" +
                "Я оста'нусь то'лько на ча'с, пото'м мне ну'жно уйти'.\n" +
                "I will stay only for an hour, then I need to leave.\n" +
                "Accusative\n" +
                "\n" +
                "Я оста'нусь споко'ен, несмо'тря на всю сумато'ху.\n" +
                "I will remain calm, despite all the hustle.\n" +
                "Short\n" +
                "\n" +
                "Все го'сти ушли', но не'которые оста'лись.\n" +
                "All the guests left, but some remained.\n" +
                "Past\n" +
                "\n" +
                "На сто'лике оста'лись то'лько пу'стые таре'лки.\n" +
                "Only empty plates remained on the table.\n" +
                "Nominative\n" +
                "\n" +
                "Мы оста'лись о'чень дово'льны результа'том.\n" +
                "We remained very pleased with the result.\n" +
                "Short\n" +
                "\n" +
                "Ма'ло вре'мени оста'лось до нача'ла спекта'кля.\n" +
                "Little time remained until the beginning of the performance.\n" +
                "Past\n" +
                "\n" +
                "От э'того зага'дочного соо'бщения оста'лось лишь недоуме'ние.\n" +
                "Only bewilderment remained from this mysterious message.\n" +
                "Nominative\n" +
                "\n" +
                "В бу'тылке оста'лось чуть-чуть воды'.\n" +
                "A little water remained in the bottle.\n" +
                "Genitive\n" +
                "\n" +
                "Она' оста'лась одна' в большо'м до'ме.\n" +
                "She remained alone in the big house.\n" +
                "Past\n" +
                "\n" +
                "На таре'лке оста'лась всего' одна' я'года.\n" +
                "Only one berry remained on the plate.\n" +
                "Nominative\n" +
                "\n" +
                "Моя' ма'ма оста'лась дово'льна пода'рком.\n" +
                "My mother remained pleased with the gift.\n" +
                "Short\n" +
                "\n" +
                "Он оста'лся в па'мяти как геро'й.\n" +
                "He remained in memory as a hero.\n" +
                "Past\n" +
                "\n" +
                "По'сле всех пра'здников у нас оста'лся то'лько сыр.\n" +
                "After all the holidays, only cheese remained for us.\n" +
                "Nominative\n" +
                "\n" +
                "Ко'тик оста'лся дово'лен свое'й но'вой игру'шкой.\n" +
                "The kitty remained pleased with its new toy.\n" +
                "Short\n" +
                "\n" +
                "Пожа'луйста, оста'ньтесь с на'ми ещё' нена'долго.\n" +
                "Please, stay with us a little longer.\n" +
                "Imperative Plural\n" +
                "\n" +
                "Оста'ньтесь на свои'х места'х до конца' филь'ма!\n" +
                "Stay in your seats until the end of the film!\n" +
                "Genitive\n" +
                "\n" +
                "Оста'ньтесь споко'йны, и всё бу'дет хорошо'.\n" +
                "Remain calm, and everything will be fine.\n" +
                "Short\n" +
                "\n" +
                "Оста'нься со мно'й, мне гру'стно.\n" +
                "Stay with me, I'm sad.\n" +
                "Imperative Singular\n" +
                "\n" +
                "Оста'нься здесь, я ско'ро верну'сь.\n" +
                "Stay here, I'll be back soon.\n" +
                "First Person Singular\n" +
                "\n" +
                "Оста'нься ве'рным свои'м идеа'лам.\n" +
                "Remain true to your ideals.\n" +
                "Dative";

        String longDefinition = "Оста'ться зна'чит не уйти' и'ли не уе'хать откуда'-то, а быть в э'том ме'сте. Э'то та'кже мо'жет зна'чить, что что'-то не испо'льзовано и'ли не ушло', и оно' всё' ещё' есть.";
        String etymology = "Сло'во \"оста'ться\" – э'то иско'нно ру'сское сло'во.\n" +
                "Оно' образова'но от глаго'ла \"стать\" и пре'фикса \"о-\".\n" +
                "Глаго'л \"стать\" зна'чит \"стоя'ть\" или \"станови'ться\". Э'то о'чень ста'рый ко'рень, кото'рый пришёл из праславя'нского языка' (*stati), а е'щё ра'ньше – из праиндоевропе'йского языка'.\n" +
                "Пре'фикс \"о-\" ча'сто пока'зывает, что де'йствие заверша'ется или что-то остаётся на ме'сте.\n" +
                "Так, \"оста'ться\" буква'льно зна'чит \"продолжа'ть стоя'ть\" или \"быть где'-то, не уходи'ть\".\n" +
                "Э'тот глаго'л испо'льзуется, когда' что-то или кто'-то не ухо'дит, а остаётся на ме'сте, или когда' что-то остаётся по'сле друго'го де'йствия.\n" +
                "Наприме'р, \"оста'ться до'ма\" (не уходи'ть) или \"оста'лось ма'ло вре'мени\" (вре'мя, кото'рое ещё' есть).\n" +
                "Э'то сло'во развива'лось есте'ственным путём в ру'сском язы'ке.";
        String usage = "Сло'во \"оста'ться\" - это о'чень распростра'нённый и нейтра'льный глаго'л в ру'сском языке'.\n" +
                "\n" +
                "*   **Форма'льный или неофициа'льный?** Оно' не форма'льное и не неофициа'льное. Вы мо'жете испо'льзовать его' в любо'й ситуа'ции: как с друзья'ми, так и с колле'гами или незнако'мыми людьми'. Оно' подхо'дит для разли'чных конте'кстов обще'ния.\n" +
                "\n" +
                "*   **Профессиона'льные или академи'ческие конте'ксты?** Да, его' мо'жно испо'льзовать и там, но оно' не специ'фическое. Наприме'р, вы мо'жете сказа'ть: \"результа'ты оста'лись те'ми же\" или \"фа'кты оста'лись без измене'ний\". Но э'то сло'во испо'льзуется не то'лько для нау'ки или рабо'ты.\n" +
                "\n" +
                "*   **Письме'нный или у'стный язык?** Оно' оди'наково ча'сто испо'льзуется как в у'стной, так и в письме'нной ре'чи. Вы услы'шите его' в повседне'вных разгово'рах и прочита'ете в кни'гах, новостя'х или пи'сьмах.\n" +
                "\n" +
                "*   **Региона'льное испо'льзование?** Нет, э'то станда'ртное сло'во. Его' испо'льзуют во всех региона'х, где говоря'т по-ру'сски. Оно' по'нятно везде'.\n" +
                "\n" +
                "*   **Социа'льные ситуа'ции и конте'ксты:**\n" +
                "    *   **\"Оста'ться где-то\"**: зна'чит не уходи'ть, быть где-то. Наприме'р: \"Мы оста'лись дома на выходны'е\". \"Он оста'лся в го'роде\".\n" +
                "    *   **\"Оста'ться каким-то\"**: зна'чит сохраня'ть состоя'ние или положе'ние. Наприме'р: \"Она' оста'лась споко'йной\". \"Вопро'с оста'лся откры'тым\".\n" +
                "    *   **\"Оста'ться что-то\"**: зна'чит быть в нали'чии, когда' что-то ушло' или испо'льзовано. Наприме'р: \"Ско'лько де'нег у тебя' оста'лось?\" \"Мне оста'лось то'лько одно' я'блоко\".\n" +
                "    *   Мо'жет означа'ть, что что-то забы'ли: \"Мои' кни'ги оста'лись в маши'не\".\n" +
                "\n" +
                "*   **Культу'рные или стилисти'ческие осо'бенности?** Э'то нейтра'льный глаго'л без осо'бых стилисти'ческих отте'нков. Он о'чень ва'жен для повседне'вного обще'ния и понима'ния ру'сского языка'.";
        String formation = "Сло'во оста́ться – э'то глаго'л в неопределённой фо'рме (инфинити'в). Дава'йте посмо'трим, как оно' стро'ится.\n" +
                "\n" +
                "1.  **Приста'вка \"о-\"**: Э'та приста'вка мо'жет пока'зывать, что де'йствие завершено' и что-то остаётся, и'ли что де'йствие происхо'дит с каки'м-то результа'том. Здесь она' помога'ет созда'ть зна'чение \"сохрани'ться\", \"быть где-то\".\n" +
                "\n" +
                "2.  **Ко'рень \"-ста-\"**: Э'тот ко'рень встреча'ется во мно'гих слова'х, свя'занных с положе'нием и'ли измене'нием положе'ния, например, в слова'х \"стоя'ть\" и'ли \"стать\". Здесь он даёт осно'вное зна'чение \"быть\", \"находи'ться\".\n" +
                "\n" +
                "3.  **Су'ффикс \"-ть\"**: Э'то су'ффикс инфинити'ва. Он пока'зывает, что э'то неопределённая фо'рма глаго'ла.\n" +
                "\n" +
                "4.  **По'стфикс \"-ся\"**: Э'то возвра'тный по'стфикс. Он пока'зывает, что де'йствие направлено на само'го себя' и'ли что де'йствие происходит само' по себе' (неперехо'дный глаго'л). В э'том слу'чае он означа'ет \"оста'ться\" как неперехо'дное де'йствие.\n" +
                "\n" +
                "Таки'м о'бразом, приста'вка \"о-\" + ко'рень \"-ста-\" + су'ффикс \"-ть\" + по'стфикс \"-ся\" образу'ют глаго'л \"оста'ться\", кото'рый означа'ет \"не уходи'ть\", \"находи'ться в определённом ме'сте\" и'ли \"быть в нали'чии\". Э'то глаго'л соверше'нного ви'да.";
        Word word = wordRepository.findById(170L).get();

        GeneratedContentDTO generatedContentDTO = new GeneratedContentDTO();
        generatedContentDTO.sentences.put(ReadingLevel.BEGINNER, beginnerSentences);
        generatedContentDTO.sentences.put(ReadingLevel.INTERMEDIATE, intermediateSentences);
        generatedContentDTO.sentences.put(ReadingLevel.ADVANCED, advancedSentences);
        generatedContentDTO.definitions = definitions;
        generatedContentDTO.wordInformation.usageContext = usage;
        generatedContentDTO.wordInformation.etymology = etymology;
        generatedContentDTO.wordInformation.formation = formation;
        generatedContentDTO.wordInformation.definition = longDefinition;

        GeneratedContentService.CorrectedContentWithErrorsDTO correctedContentWithErrorsDTO = generatedContentService.createCorrectedContentEntities(generatedContentDTO, word);
        wordService.saveGeneratedContentToWord(
                correctedContentWithErrorsDTO.sentenceWithErrors.stream().map((s) -> s.sentence).toList(),
                correctedContentWithErrorsDTO.definitionWithErrors.stream().map((d) -> d.definition).toList(),
                correctedContentWithErrorsDTO.wordInformation,
                word);
        generatedContentErrorService.addErrors(correctedContentWithErrorsDTO.sentenceWithErrors, correctedContentWithErrorsDTO.definitionWithErrors);

        List<Long> allSentenceIds = word.getSentences().stream().map(Sentence::getId).toList();
        List<GeneratedContentError> allErrorsInDB = new ArrayList<>();
        allSentenceIds.stream().map((sentenceID) -> generatedContentErrorRepository.findAllByOriginatingEntityId(sentenceID))
                        .forEach((allErrorsInDB::addAll));


        Assertions.assertAll(
                () -> Assertions.assertEquals(1, allErrorsInDB.size()),
                () -> Assertions.assertFalse(sentenceRepository.findAllByWordId(170L).isEmpty()),
                () -> Assertions.assertFalse(definitionRepository.findAllByWordId(170L).isEmpty()),
                () -> Assertions.assertNotNull(wordInformationRepository.findByWordId(170L))
        );
    }


}
