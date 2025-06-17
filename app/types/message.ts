export interface Message {
  id?: number
  expediteurId: number
  conversationId: number
  contenu: string
  createdAt?: Date
  updatedAt?: Date
} 