import type { RedirectRequest } from '@azure/msal-browser'

//const clientId = import.meta.env.VITE_CLIENT_ID ?? ''
const apiScope = `api://262adf80-2a1e-4193-a2bc-df28650eb1bc/access_as_user`

/** OIDC + scope delegado hacia msal-api. */
export const loginRequest: RedirectRequest = {
  scopes: ['openid', 'profile', apiScope],
}