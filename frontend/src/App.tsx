import { useState } from 'react'
import { submitMessage, type AnalysisResponse } from './api'
import './App.css'

function App() {
  const [input, setInput] = useState('')
  const [loading, setLoading] = useState(false)
  const [result, setResult] = useState<AnalysisResponse | null>(null)
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!input.trim()) return

    setLoading(true)
    setResult(null)
    setError(null)

    try {
      const response = await submitMessage(input.trim())
      setResult(response)
      if (response.accepted) setInput('')
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Something went wrong')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="container">
      <h1>Be Positive ✨</h1>
      <p className="subtitle">Submit a message and our AI will check if it spreads positivity.</p>

      <form onSubmit={handleSubmit} className="form">
        <textarea
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Type your message here..."
          rows={3}
          disabled={loading}
        />
        <button type="submit" disabled={loading || !input.trim()}>
          {loading ? 'Analyzing...' : 'Submit'}
        </button>
      </form>

      {error && (
        <div className="result error">
          <p>{error}</p>
        </div>
      )}

      {result && result.accepted && (
        <div className="result success">
          <h2>Accepted!</h2>
          <p>{result.reason}</p>
        </div>
      )}

      {result && !result.accepted && (
        <div className="result rejected">
          <h2>Not Positive Enough</h2>
          <p><strong>Reason:</strong> {result.reason}</p>
          {result.suggestedRewrite && (
            <p><strong>Try instead:</strong> {result.suggestedRewrite}</p>
          )}
        </div>
      )}
    </div>
  )
}

export default App
