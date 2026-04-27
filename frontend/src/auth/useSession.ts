import { useSyncExternalStore } from 'react';
import { subscribe, getSession, type Session } from './auth';

export function useSession(): Session | null {
  return useSyncExternalStore<Session | null>(subscribe, getSession, () => null);
}
