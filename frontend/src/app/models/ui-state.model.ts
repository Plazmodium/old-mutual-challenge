export type UiStatus = 'idle' | 'loading' | 'success' | 'error';

export interface IUiState {
  status: UiStatus;
  message: string | null;
  code?: number | null;
}

export const initialUiState: IUiState = {
  status: 'idle',
  message: null,
  code: null
};
