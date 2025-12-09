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
        }

        prompt.append("You will be creating Russian grammar exercises using different forms of the word "
                + word.getAccented() + ". You need to write sentences at " + level + " level Russian proficiency using each specified form of the word.\n" +
                "Here are the word forms you must use:\n");


        for (int i = 0; i < forms.size(); i++) {
            if (forms.get(i).getBare() == null) {
                continue;
            }

            switch (forms.get(i).getFormType()) {
                case "ru_base":
                    prompt.append("Base: ");
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
                You can only use these word forms . For each word form listed above, you must create 2-3 unique sentences. Each sentence should be followed by its English translation.
                
                Requirements:
                """ + " - Write sentences at ").append(level).append(" level Russian").append(""" 
                - Each sentence must be grammatically correct
                - Include stress marks (') on ALL Russian words where needed, not just the target word. The stress is very important, do not forget.
                - Make sentences diverse and interesting - some can be humorous or absurd for better memory retention
                - Every sentence should be completely different and unique
                - Do not write any numbers, explanations, or additional information
                
                Format your output exactly like this example:
                Кресть'яне торгу'ют зе'млями уже' не'сколько лет.
                Peasants have been trading lands for several years.
                
                Each sentence and its translation should be on separate lines. Work through each word form in the order provided, creating 2-3 sentences for each form. Include every single form listed in the word forms.
                
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
}
