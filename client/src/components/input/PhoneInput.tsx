import React from "react";
import { BaseInput } from "./BaseInput";

type PhoneInputProps = {
  value: string;
  onChange: (value: string) => void;
  error?: string;
  label?: string;
  placeholder?: string;
};

export const PhoneInput: React.FC<PhoneInputProps> = ({
  value,
  onChange,
  error,
  label,
  placeholder = "+375 ",
}) => {
  const formatDisplay = (val: string) => {
    const digits = val.replace(/\D/g, "").slice(3, 12);
    if (!digits) return "+375 ";
    let formatted = "+375";
    if (digits.length >= 2) formatted += ` ${digits.slice(0, 2)}`;
    else formatted += ` ${digits.slice(0, 2)}`;
    if (digits.length > 2) formatted += ` ${digits.slice(2, 5)}`;
    if (digits.length > 5) formatted += `-${digits.slice(5, 7)}`;
    if (digits.length > 7) formatted += `-${digits.slice(7, 9)}`;
    return formatted;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    let digits = e.target.value.replace(/\D/g, "");
    if (!digits.startsWith("375")) digits = "375" + digits.slice(3);
    digits = digits.slice(0, 12);
    onChange("+" + digits);
  };

  return (
    <BaseInput
      label={label}
      value={formatDisplay(value)}
      onChange={handleChange}
      error={error}
      placeholder={placeholder}
    />
  );
};