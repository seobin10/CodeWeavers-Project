import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  isOpen: false,
  message: "",
  confirm: {
    isOpen: false,
    message: "",
    onConfirm: null,
  },
};

const modalSlice = createSlice({
  name: "modal",
  initialState,
  reducers: {
    showModal: (state, action) => {
      state.isOpen = true;
      state.message = action.payload;
    },
    hideModal: (state) => {
      state.isOpen = false;
      state.message = "";
    },
    showConfirm: (state, action) => {
      state.confirm.isOpen = true;
      state.confirm.message = action.payload.message;
      state.confirm.onConfirm = action.payload.onConfirm;
    },
    hideConfirm: (state) => {
      state.confirm.isOpen = false;
      state.confirm.message = "";
      state.confirm.onConfirm = null;
    },
  },
});

export const { showModal, hideModal, showConfirm, hideConfirm } =
  modalSlice.actions;
export default modalSlice.reducer;



