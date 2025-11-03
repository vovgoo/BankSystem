import React, { useState, useEffect } from "react";
import { BaseInput } from "./BaseInput";

type NumberInputProps = {
  value: number;
  onChange: (value: number) => void;
  error?: string;
  label?: string;
  placeholder?: string;
};

export const NumberInput: React.FC<NumberInputProps> = ({
  value,
  onChange,
  error,
  label,
  placeholder,
}) => {
  const [displayValue, setDisplayValue] = useState(value.toString());

  useEffect(() => {
    setDisplayValue(value.toString());
  }, [value]);

  const handleChange = (val: string) => {
    if (/^\d*\.?\d*$/.test(val)) {
      setDisplayValue(val);
      const parsed = parseFloat(val);
      if (!isNaN(parsed)) {
        onChange(parsed); 
      }
    }
  };

  return (
    <BaseInput
      label={label}
      value={displayValue}
      onChange={(e) => handleChange(e.target.value)}
      error={error}
      placeholder={placeholder}
    />
  );
};