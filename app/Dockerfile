# Build stage
FROM node:18-alpine as build-stage

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers de dépendances
COPY package*.json ./

# Installer les dépendances
RUN npm ci --only=production

# Copier le reste du code source
COPY . .

# Build de l'application
RUN npm run build

# Production stage
FROM node:18-alpine as production-stage

# Créer un utilisateur non-root pour la sécurité
RUN addgroup -g 1001 -S nodejs
RUN adduser -S nuxt -u 1001

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers nécessaires depuis le build stage
COPY --from=build-stage --chown=nuxt:nodejs /app/.output ./.output
COPY --from=build-stage --chown=nuxt:nodejs /app/package*.json ./

# Installer seulement les dépendances de production
RUN npm ci --only=production && npm cache clean --force

# Changer vers l'utilisateur non-root
USER nuxt

# Exposer le port
EXPOSE 3000

# Commande pour démarrer l'application
CMD ["node", ".output/server/index.mjs"] 