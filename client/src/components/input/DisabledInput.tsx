import React from "react";
import { BaseInput } from "./BaseInput";

type DisabledInputProps = {
  value: string;
  label?: string;
};

export const DisabledInput: React.FC<DisabledInputProps> = ({ value, label }) => {
  return <BaseInput label={label} value={value} disabled />;
};