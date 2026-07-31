package guru.bonacci.bepositive;

public record AnalysisResult(boolean accepted, String reason, String suggestedRewrite) {
}
