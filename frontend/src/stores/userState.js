var listeners = [];
var state = { authenticated: false };

const emitChange = () => {
  for (var listener of listeners) {
    listener();
  }
};

export const userState = {
  set(newState) {
    state = {
      ...state,
      ...newState,
    };
    emitChange();
  },
  subscribe(listener) {
    listeners = [...listeners, listener];
    return () => {
      listeners = listeners.filter((l) => l !== listener);
    };
  },
  get() {
    return state;
  },
};
