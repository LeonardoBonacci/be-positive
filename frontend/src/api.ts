export interface AnalysisResponse {
  accepted: boolean;
  reason: string;
  suggestedRewrite?: string;
  message?: {
    id: string;
    content: string;
    timestamp: string;
  };
}

const API_BASE = "http://localhost:8080/api";

export async function submitMessage(content: string): Promise<AnalysisResponse> {
  const response = await fetch(`${API_BASE}/messages`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ content }),
  });

  if (!response.ok) {
    throw new Error(`Server error: ${response.status}`);
  }

  return response.json();
}
