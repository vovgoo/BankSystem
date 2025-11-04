import React from 'react';
import { BaseInput } from './BaseInput';

type TextInputProps = {
  value: string;
  onChange: (value: string) => void;
  error?: string;
  label?: string;
  type?: string;
  placeholder?: string;
};

export const TextInput: React.FC<TextInputProps> = ({
  value,
  onChange,
  error,
  label,
  type = 'text',
  placeholder,
}) => {
  return (
    <BaseInput
      label={label}
      value={value}
      onChange={(e) => onChange(e.target.value)}
      error={error}
      type={type}
      placeholder={placeholder}
    />
  );
};
