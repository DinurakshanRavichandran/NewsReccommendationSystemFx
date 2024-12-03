package com.example.oodprojectfx.services;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class NLPPipeline {
    private static Properties properties;
    private static String propertiesName = "tokenize, ssplit, pos, lemma, ner";
    private static StanfordCoreNLP stanfordCoreNLP;

    // Singleton instance
    private static final NLPPipeline instance = new NLPPipeline();

    // Private constructor to prevent external instantiation
    private NLPPipeline() {
        properties = new Properties();
        properties.setProperty("annotators", propertiesName);
    }

    // Public method to get the singleton instance
    public static NLPPipeline getInstance() {
        return instance;
    }

    // Lazy initialization of the StanfordCoreNLP pipeline
    public static StanfordCoreNLP getPipeline() {
        if (stanfordCoreNLP == null) {
            stanfordCoreNLP = new StanfordCoreNLP(properties);
        }
        return stanfordCoreNLP;
    }

    public String categorize(String article) {
        // Define categories and their keywords
        // Technology Keywords
        String[] technologyKeywords = {
                "AI", "algorithm", "analytics", "app", "artificial", "automation", "backend", "big data", "blockchain", "browser",
                "chip", "cloud", "code", "coding", "computer", "computing", "cybersecurity", "data", "database", "design",
                "developer", "digital", "electronics", "engineering", "firmware", "frontend", "hardware", "innovation", "internet", "IoT",
                "IT", "Java", "machine learning", "microchip", "mobile", "network", "OS", "programming", "Python", "quantum",
                "robotics", "SaaS", "satellite", "science", "semiconductor", "simulation", "smartphone", "software", "space", "storage",
                "supercomputer", "tech", "technology", "Tesla", "UI", "UX", "virtual", "VR", "website", "wireless",
                "5G", "3D printing", "antivirus", "API", "augmented", "automation", "autonomous", "bandwidth", "coding", "compiler",
                "CPU", "cryptocurrency", "cybersecurity", "deep learning", "digitalization", "drone", "encryption", "framework", "front-end", "gadget",
                "GPU", "HTML", "innovation", "IT", "JavaScript", "Linux", "microprocessor", "ML", "network", "Python",
                "satellite", "server", "smart", "startup"
        };

// Sports Keywords
        String[] sportsKeywords = {
                "athlete", "athletics", "badminton", "ball", "baseball", "basketball", "bench", "bike", "biking", "boxing",
                "captain", "championship", "coach", "competition", "cricket", "cycling", "defense", "diving", "endurance", "exercise",
                "FIFA", "field", "football", "game", "goal", "gold", "gym", "gymnastics", "hockey", "injury",
                "IPL", "jump", "league", "marathon", "match", "medal", "NFL", "NHL", "Olympics", "pitch",
                "player", "pole vault", "practice", "professional", "race", "referee", "running", "score", "soccer", "sport",
                "sprint", "stadium", "swimming", "table tennis", "team", "tennis", "tournament", "training", "volleyball", "win",
                "wrestling", "yoga", "archery", "base runner", "batter", "bowling", "catcher", "cheerleading", "climbing", "coach",
                "corner", "crossfit", "defense", "double play", "draft", "field goal", "fitness", "foul", "freestyle", "goalkeeper"
        };

// Politics Keywords
        String[] politicsKeywords = {
                "administration", "agreement", "ambassador", "bill", "campaign", "candidate", "caucus", "citizen", "coalition", "constitution",
                "court", "debate", "democracy", "diplomacy", "diplomat", "election", "embassy", "government", "governor", "house",
                "impeachment", "justice", "law", "leader", "legislation", "liberal", "minister", "monarchy", "nation", "parliament",
                "party", "political", "politician", "poll", "president", "prime minister", "reform", "republic", "senate", "senator",
                "state", "supreme court", "tax", "treaty", "union", "vote", "voter", "voting", "whip", "policy",
                "assembly", "autonomy", "ballot", "bureaucracy", "campaigning", "coalition", "communism", "conservative", "constituency", "delegation",
                "dissent", "federal", "governmental", "ideology", "judicial", "legislative", "lobby", "manifesto", "ministerial", "nationalism",
                "policy-making", "politburo", "referendum", "socialism", "sovereignty", "strategy", "subsidy", "suffrage", "tactics", "trust"
        };

// Celebrity Keywords
        String[] celebrityKeywords = {
                "actor", "actress", "award", "biography", "celebrity", "cinema", "director", "drama", "fashion", "fame",
                "famous", "film", "gossip", "Grammy", "Hollywood", "interview", "magazine", "model", "movie", "music",
                "musician", "Oscars", "paparazzi", "performance", "pop", "publicity", "reality", "record", "red carpet", "singer",
                "socialite", "song", "star", "stardom", "style", "superstar", "talent", "theater", "trend", "TV",
                "vlog", "vlogger", "YouTube", "autograph", "beauty", "blockbuster", "blog", "celebrate", "charity", "fashionista",
                "fandom", "fan", "followers", "icon", "influencer", "limelight", "luxury", "media", "millionaire", "modelling",
                "personal life", "photo shoot", "reality show", "remix", "romantic", "selfie", "showbiz", "social media", "spotlight", "studio",
                "subscription", "TikTok", "trending", "vanity", "viral"
        };

// Business Keywords
        String[] businessKeywords = {
                "accounting", "advertisement", "assets", "balance", "bank", "branding", "business", "capital", "cash", "CEO",
                "clients", "commerce", "company", "contract", "corporate", "customer", "deal", "debt", "economics", "entrepreneur",
                "equity", "finance", "financial", "firm", "fund", "global", "growth", "industry", "investment", "IPO",
                "loan", "management", "market", "marketing", "merger", "money", "net", "organization", "owner", "partnership",
                "pay", "payment", "profit", "project", "revenue", "risk", "sales", "shareholder", "shares", "startup",
                "strategy", "stock", "success", "supply", "tax", "team", "trade", "trading", "transaction", "valuation",
                "venture", "wealth", "businessman", "businesswoman", "cash flow", "consumer", "contractor", "corporation", "cost", "economic",
                "funding", "growth strategy", "income", "innovation", "insurance", "invoice", "leadership", "logistics", "management team", "procurement"
        };


        // Initialize scores for each category
        int[] scores = new int[5]; // Index 0: Technology, 1: Sports, 2: Politics, 3: Celebrity, 4: Business

        // Prepare the document for annotation
        CoreDocument coreDocument = new CoreDocument(article);
        getPipeline().annotate(coreDocument);

        // Process tokens concurrently
        coreDocument.tokens().parallelStream().forEach(token -> {
            String word = token.word().toLowerCase();
            if (containsWord(word, technologyKeywords)) synchronized (scores) { scores[0]++; }
            if (containsWord(word, sportsKeywords)) synchronized (scores) { scores[1]++; }
            if (containsWord(word, politicsKeywords)) synchronized (scores) { scores[2]++; }
            if (containsWord(word, celebrityKeywords)) synchronized (scores) { scores[3]++; }
            if (containsWord(word, businessKeywords)) synchronized (scores) { scores[4]++; }
        });

        // Determine the highest-scoring category
        int maxScoreIndex = 0;
        for (int i = 1; i < scores.length; i++) {
            if (scores[i] > scores[maxScoreIndex]) {
                maxScoreIndex = i;
            }
        }

        // Return the category based on the highest score
        switch (maxScoreIndex) {
            case 0: return "Technology";
            case 1: return "Sports";
            case 2: return "Politics";
            case 3: return "Celebrity";
            case 4: return "Business";
            default: return "Uncategorized";
        }
    }

    private boolean containsWord(String word, String[] keywords) {
        for (String keyword : keywords) {
            if (word.equals(keyword)) {
                return true;
            }
        }
        return false;
    }



}
