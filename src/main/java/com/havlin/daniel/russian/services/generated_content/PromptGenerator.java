package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordForm;
import com.havlin.daniel.russian.entities.generated_content.ReadingLevel;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;

import java.util.List;

public class PromptGenerator {
    private final WordFormRepository wordFormRepository;
    
    public PromptGenerator(WordFormRepository wordFormRepository) {
        this.wordFormRepository = wordFormRepository;
    }
    
    /**
     * Generates the prompt used for creating the sentences. This will inform the LLM of every grammatical form that the word can take and also sets the reading level. It does its best to keep the LLM on track.
     * @param word The word entity is required over string because all the word forms are pull from the database
     * @param readingLevel Sets the reading level from B1-C1.
     * @return A long string, spanning many lines which contain all the sentences the LLM could produce.
     */
    String buildPromptForSentenceGeneration(Word word, ReadingLevel readingLevel) {
        StringBuilder prompt = new StringBuilder();
        List<WordForm> forms = wordFormRepository.getAllByWordId(word.getId());

        String level = "";
        if (readingLevel == ReadingLevel.BEGINNER) {
            level = ("B1");
        } else if (readingLevel == ReadingLevel.INTERMEDIATE) {
            level = "B2";
        } else {
            level = "C1";
        }

        prompt.append("You will be creating Russian grammar exercises using different forms of the word "
                + word.getAccented() + ". You need to write sentences at " + level + " level Russian proficiency using each specified form of the word.\n" +
                "Here are the word forms you must use:\n");


        for (int i = 0; i < forms.size(); i++) {
            if (forms.get(i).getBare() == null) {
                continue;
            }

            switch (forms.get(i).getFormType()) {
                // Verbs
                case "ru_verb_past_m", "ru_verb_past_f", "ru_verb_past_n", "ru_verb_past_pl":
                    prompt.append("Past: ");
                    break;
                case "ru_verb_presfut_sg1":
                    prompt.append("First Person Singular: " );
                    break;
                case "ru_verb_presfut_sg2":
                    prompt.append("Second Person Singular: " );
                    break;
                case "ru_verb_presfut_sg3":
                    prompt.append("Third Person Singular: " );
                    break;
                case "ru_verb_presfut_pl1":
                    prompt.append("First Person Plural: " );
                    break;
                case "ru_verb_presfut_pl2":
                    prompt.append("Second Person Plural: " );
                    break;
                case "ru_verb_presfut_pl3":
                    prompt.append("Third Person Plural: " );
                    break;
                case "ru_verb_gerund_present":
                    prompt.append("Gerund Present: " );
                    break;
                case "ru_verb_gerund_past":
                    prompt.append("Gerund Past: ");
                    break;
                case "ru_verb_participle_active_present":
                    prompt.append("Participle Active Present: " );
                    break;
                case "ru_verb_participle_active_past":
                    prompt.append("Participle Active Past: ");
                    break;
                case "ru_verb_participle_passive_present":
                    prompt.append("Participle Passive Present: ");
                    break;
                case "ru_verb_participle_passive_past":
                    prompt.append("Participle Passive Past: ");
                    break;
                case "ru_verb_imperative_sg":
                    prompt.append("Imperative Singular: ");
                    break;
                case "ru_verb_imperative_pl":
                    prompt.append("Imperative Plural: ");
                    break;
                // Nouns and Adjectives
                case "ru_noun_sg_nom", "ru_noun_pl_nom", "ru_adj_m_nom", "ru_adj_f_nom", "ru_adj_n_nom", "ru_adj_pl_nom":
                    prompt.append("Nominative: ");
                    break;
                case "ru_noun_sg_gen", "ru_noun_pl_gen", "ru_adj_m_gen", "ru_adj_f_gen", "ru_adj_n_gen", "ru_adj_pl_gen":
                    prompt.append("Genitive: ");
                    break;
                case "ru_noun_sg_dat", "ru_noun_pl_dat", "ru_adj_m_dat", "ru_adj_f_dat", "ru_adj_n_dat", "ru_adj_pl_dat":
                    prompt.append("Dative: ");
                    break;
                case "ru_noun_sg_acc", "ru_noun_pl_acc", "ru_adj_m_acc", "ru_adj_f_acc", "ru_adj_n_acc", "ru_adj_pl_acc":
                    prompt.append("Accusative: ");
                    break;
                case "ru_noun_sg_inst", "ru_noun_pl_inst", "ru_adj_m_inst", "ru_adj_f_inst", "ru_adj_n_inst", "ru_adj_pl_inst":
                    prompt.append("Instrumental: ");
                    break;
                case "ru_noun_sg_prep", "ru_noun_pl_prep", "ru_adj_m_prep", "ru_adj_f_prep", "ru_adj_n_prep", "ru_adj_pl_prep":
                    prompt.append("Prepositional: ");
                    break;
                case "ru_adj_short_m", "ru_adj_short_f", "ru_adj_short_n", "ru_adj_short_pl":
                    prompt.append("Short: ");
                    break;
                case "ru_adj_comparative":
                    prompt.append("Comparative: ");
                    break;
                case "ru_adj_superlative":
                    prompt.append("Superlative: ");
                    break;
            }

            prompt.append(forms.get(i).getAccented());
            if (i == forms.size() - 1) {
                prompt.append(". \n");
            } else {
                prompt.append(", \n");
            }
        }

        prompt.append("""
                For each word form listed above, you must create 2-3 unique sentences. Each sentence should be followed by its English translation, then the grammatical case/form (without mentioning the specific word, gender, number, or using the word "case").
                
                Requirements:
                """ + " - Write sentences at ").append(level).append(" level Russian").append(""" 
                - Each sentence must be grammatically correct
                - Include stress marks (') on ALL Russian words where needed, not just the target word. The stress is very important, do not forget.
                - Make sentences diverse and interesting - some can be humorous or absurd for better memory retention
                - Every sentence should be completely different and unique
                - Do not write any numbers, explanations, or additional information
                - Do not mention plural, singular, gender, or write "case" in your grammar descriptions
                - Only identify the grammatical form (e.g., "Instrumental", "Genitive", "Short")
                
                Format your output exactly like this example:
                Кресть'яне торгу'ют зе'млями уже' не'сколько лет.
                Peasants have been trading lands for several years.
                Instrumental
                
                Each sentence, its translation, and grammar form should be on separate lines. Work through each word form in the order provided, creating 2-3 sentences for each form. Include every single form listed in the word forms.
                
                Begin writing the sentences immediately without any preamble or additional text.
                """);

        return prompt.toString();
    }

    public String getShortDefinitionPrompt(Word word) {
        return "You need to write 3 short one-sentence definitions in Russian for the following word:\n" +
                "\n" +
                 word.getAccented() +
                "\n" +
                "Your task is to create three different definitions that each meet these specific requirements:\n" +
                "\n" +
                "- Write each definition in B1 level Russian (intermediate level). This means:\n" +
                "  * Use clear, accessible vocabulary that an intermediate Russian learner would understand\n" +
                "  * Use straightforward grammar structures\n" +
                "  * Avoid complex literary expressions, technical jargon, or advanced vocabulary\n" +
                "  * Think of the level of language used in textbooks for intermediate students\n" +
                "\n" +
                "- Each definition must be exactly one sentence\n" +
                "- Mark the stress in your definitions by putting an apostrophe (') immediately after each stressed vowel (for example: \"э'то\", \"де'рево\", \"краси'вый\")\n" +
                "- Do not use the word itself anywhere in any of your definitions\n" +
                "- Do not include a dash (—) anywhere in your definitions\n" +
                "- Each definition should offer a different perspective or aspect of the word's meaning\n" +
                "- Put each definition on a separate line\n" +
                "\n" +
                "Example format:\n" +
                "Большо'е живо'тное, кото'рое живёт в ле'су и е'ст ры'бу.\n" +
                "Си'льный зверь с густо'й ше'рстью и больши'ми ла'пами.\n" +
                "Косма'тое существо', кото'рое спит всю зи'му.";
    }

    public String getLongDefinitionPrompt(Word word) {
        return "You will be writing a comprehensive definition in Russian for the following word:\n" +
                "\n" +
                "<word>\n" +
                word.getAccented() +
                "</word>\n" +
                "\n" +
                "Your task is to create a detailed definition of this word in Russian that is appropriate for B1 level Russian learners. Your definition must include all four of the following components:\n" +
                "\n" +
                "1. **Основное определение (Main definition)**: Explain what the word means using clear, accessible Russian vocabulary and sentence structures suitable for intermediate learners. Avoid overly complex grammatical constructions or advanced vocabulary.\n" +
                "\n" +
                "2. **Этимология/Происхождение (Etymology/Origin)**: If known, explain the origin of the word. This might include information about whether it was borrowed from another language (например, из английского языка), derived from Old Church Slavonic (из церковнославянского), formed from other Russian roots, etc. If the etymology is uncertain or unknown, state this clearly.\n" +
                "\n" +
                "3. **Контекст употребления (Usage context)**: Describe the situations, contexts, or registers where this word is typically used. For example, is it formal or informal? Is it used in specific professional contexts? Is it more common in written or spoken language? Is it used in particular regions?\n" +
                "\n" +
                "4. **Морфемный состав (Word formation)**: Analyze how the word is constructed by identifying its stems (корни), prefixes (приставки), and suffixes (суффиксы). Explain how these parts contribute to the word's meaning.\n" +
                "\n" +
                "**Critical formatting requirements:**\n" +
                "- Each component should be on it's own line without using a header or the name of the component." +
                "- The components should always be printed in the order given" +
                "- Mark stress on ALL Russian words by placing an apostrophe (') immediately after the stressed vowel (for example: слово́ should be written as слово')\n" +
                "- Write your entire response in Russian\n" +
                "- Use vocabulary and grammar appropriate for B1 level learners\n" +
                "- Organize your response in clear, well-structured paragraphs\n" +
                "- Do not include the word itself as a header\n" +
                "\n" +
                "**Language level guidelines:**\n" +
                "- Use common, everyday vocabulary when possible\n" +
                "- Keep sentence structures relatively simple\n" +
                "- Explain any technical linguistic terms you use\n" +
                "- Write in a clear, educational tone suitable for language learners\n" +
                "\n" +
                "Begin your definition immediately without any preamble or introduction.";
    }

    public String[] getWordInformationPrompts(Word word) {
        String[] prompts = new String[4];

        // Long definition
        prompts[0] = "You will be writing a definition in Russian for the following word:\n" +
                "\n" +
                "<word>\n" +
                word.getAccented() + "\n" +
                "</word>\n" +
                "\n" +
                "Your task is to write a definition of this word in B1 level Russian. The definition should be:\n" +
                "- A couple of sentences long\n" +
                "- No more than one paragraph\n" +
                "- Written at an intermediate (B1) level of Russian, using vocabulary and grammar structures appropriate for that level\n" +
                "- Clear and easy to understand\n" +
                "\n" +
                "Important formatting requirement: In every word of your definition where a letter could potentially be stressed, you must put an apostrophe (') immediately after the stressed vowel letter. This applies to all words in your definition that have more than one syllable.\n" +
                "\n" +
                "For example:\n" +
                "- \"слово́\" should be written as \"сло'во\"\n" +
                "- \"определе́ние\" should be written as \"определе'ние\" \n" +
                "- \"говори́ть\" should be written as \"говори'ть\"\n" +
                "\n" +
                "Single-syllable words do not need stress marks.";

        // Etylomogy
        prompts[1] = "You will be writing the etymology of a Russian word in B1 level Russian.\n" +
                "\n" +
                "Here is the word:\n" +
                "<word>\n" +
                word.getAccented() + "\n" +
                "</word>\n" +
                "\n" +
                "Your task is to explain the origin and etymology of this word in Russian at a B1 proficiency level. Your explanation should include:\n" +
                "\n" +
                "- The origin of the word if known (for example, whether it was borrowed from another language like English - \"из английского языка\", derived from Old Church Slavonic - \"из церковнославянского\", formed from other Russian roots, etc.)\n" +
                "- Any relevant historical or linguistic information about how the word developed\n" +
                "- If the etymology is uncertain or unknown, clearly state this\n" +
                "\n" +
                "Critical formatting requirement: In every word of your response where a letter could potentially be stressed, you must put an apostrophe (') immediately after the stressed vowel letter. This applies to all Russian words in your explanation that have more than one syllable.\n" +
                "\n" +
                "Examples of correct stress marking:\n" +
                "- \"слово́\" should be written as \"сло'во\"\n" +
                "- \"определе́ние\" should be written as \"определе'ние\" \n" +
                "- \"говори́ть\" should be written as \"говори'ть\"\n" +
                "- \"английского\" should be written as \"англи'йского\"\n" +
                "- \"церковнославянского\" should be written as \"церковнославя'нского\"\n" +
                "\n" +
                "Single-syllable words do not need stress marks.\n" +
                "\n" +
                "Write in clear, accessible Russian appropriate for B1 level learners.";

        // Usage Context
        prompts[2] = "You will be writing a usage context description for a Russian word at B1 level.\n" +
                "\n" +
                "Here is the Russian word:\n" +
                "<word>\n" +
                word.getAccented() + "\n" +
                "</word>\n" +
                "\n" +
                "Your task is to describe in Russian the situations, contexts, or registers where this word is typically used. Consider and address the following aspects:\n" +
                "\n" +
                "- Is the word formal (официа'льный) or informal (неофициа'льный)?\n" +
                "- Is it used in specific professional or academic contexts?\n" +
                "- Is it more common in written (письме'нный) or spoken (у'стный) language?\n" +
                "- Is it used in particular regions or is it standard across Russian-speaking areas?\n" +
                "- What social situations or communication contexts is it appropriate for?\n" +
                "- Are there any cultural or stylistic considerations?\n" +
                "\n" +
                "Write your response in Russian at a B1 level, using clear and accessible language that would be appropriate for intermediate Russian learners.\n" +
                "\n" +
                "CRITICAL FORMATTING REQUIREMENT: In every word of your response that has more than one syllable, you must put an apostrophe (') immediately after the stressed vowel letter. This applies to ALL multi-syllabic words in your description.\n" +
                "\n" +
                "Examples of correct stress marking:\n" +
                "- \"определе'ние\" (not \"определение\")\n" +
                "- \"ситуа'ция\" (not \"ситуация\") \n" +
                "- \"испо'льзуется\" (not \"используется\")\n" +
                "- \"письме'нный\" (not \"письменный\")\n" +
                "\n" +
                "Single-syllable words like \"в\", \"на\", \"это\", \"тот\" do not need stress marks.";

        // Word Formation
        prompts[3] = "You will be analyzing the word formation of a Russian word at B1 level. Here is the word to analyze:\n" +
                "\n" +
                "<word>\n" +
                word.getAccented() + "\n" +
                "</word>\n" +
                "\n" +
                "Your task is to write a clear explanation in Russian about how this word is constructed. You should:\n" +
                "\n" +
                "1. Identify and explain the word's components:\n" +
                "   - Stem(s) (корни) - the main meaningful part(s) of the word\n" +
                "   - Prefixes (приставки) - parts added before the stem\n" +
                "   - Suffixes (суффиксы) - parts added after the stem\n" +
                "   - Ending (окончание) if applicable\n" +
                "\n" +
                "2. Explain how these parts work together to create the word's meaning\n" +
                "\n" +
                "3. Write your explanation at B1 level - use clear, accessible language that an intermediate Russian learner would understand\n" +
                "\n" +
                "CRITICAL FORMATTING REQUIREMENT: You must mark stress in ALL multi-syllable words in your response. Place an apostrophe (') immediately after the stressed vowel letter in every word that has more than one syllable. Single-syllable words do not need stress marks.\n" +
                "\n" +
                "Examples of correct stress marking:\n" +
                "- \"определе́ние\" → \"определе'ние\"\n" +
                "- \"приста́вка\" → \"приста'вка\" \n" +
                "- \"суффи́кс\" → \"суффи'кс\"\n" +
                "- \"зна́чение\" → \"зна'чение\"\n" +
                "- \"образова́н\" → \"образо'ван\"\n" +
                "\n" +
                "Do not mark stress in single-syllable words like \"корь\", \"дом\", \"сад\".\n" +
                "\n" +
                "Write your complete analysis in Russian, ensuring every multi-syllable word has proper stress marking with apostrophes.\n" +
                "\n" +
                "Do not include headers";

        return prompts;
    }
}
