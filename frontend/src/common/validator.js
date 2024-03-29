import React from 'react';
import { isEmail } from "validator";
import {  USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH,
          NAME_MIN_LENGTH, NAME_MAX_LENGTH,
          PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH } from "./constants";

export const isRequired = value => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        This field is required!
      </div>
    );
  }
};

export const isEmailFormat = value => {
  if (!isEmail(value)) {
    return (
      <div className="alert alert-danger" role="alert">
        This is not a valid email.
      </div>
    );
  }
};

export const isUsername = value => {
  if (value.length < USERNAME_MIN_LENGTH || value.length > USERNAME_MAX_LENGTH) {
    return (
      <div className="alert alert-danger" role="alert">
        The username must be between {USERNAME_MIN_LENGTH} and {USERNAME_MAX_LENGTH} characters.
      </div>
    );
  }
};

export const isFullname = value => {
  if (value.length < NAME_MIN_LENGTH || value.length >= NAME_MAX_LENGTH) {
    return (
      <div className="alert alert-danger" role="alert">
        Your name must be between {NAME_MIN_LENGTH} and {NAME_MAX_LENGTH} characters.
      </div>
    );
  }
};

export const isPassword = value => {
  if (value.length < PASSWORD_MIN_LENGTH || value.length > PASSWORD_MAX_LENGTH) {
    return (
      <div className="alert alert-danger" role="alert">
        The password must be between {PASSWORD_MIN_LENGTH} and {PASSWORD_MAX_LENGTH} characters.
      </div>
    );
  }
};
