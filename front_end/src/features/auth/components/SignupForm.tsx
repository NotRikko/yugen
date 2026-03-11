"use client"

import {
  useForm
} from "react-hook-form"
import {
  zodResolver
} from "@hookform/resolvers/zod"
import * as z from "zod"
import { Button } from "@/shared/ui/button"
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/shared/ui/form"
import { Input } from "@/shared/ui/input"
import { Checkbox } from "@/shared/ui/checkbox"
import { useNavigate } from "react-router-dom"
import { useState } from "react"

 
const formSchema = z.object({
  username: z.string().min(4, "Username must be at least 4 characters").max(25),
  displayName: z.string().min(4, "Display name must be at least 4 characters").max(25),
  email: z.string().email("Invalid email format"),
  password: z.string().min(8, "Password must be at least 8 characters").max(30),
  confirm: z.string().min(8),
  isArtist: z.boolean().default(false),
}).refine((data) => data.password === data.confirm, {
  path: ["confirm"],
  message: "Passwords don't match",
});

export default function SignupForm() {
  const navigate = useNavigate();
  const [formError, setFormError] = useState<string | null>(null);

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      username: "",
      displayName: "",
      email: "",
      password: "",
      confirm: "",
      isArtist: false,
    },
  });
  const API_URL = import.meta.env.VITE_API_URL;

  async function onSubmit(values: z.infer<typeof formSchema>) {
    setFormError(null);
  
    const payload = {
      username: values.username,
      displayName: values.displayName,
      email: values.email,
      password: values.password,
      isArtist: Boolean(values.isArtist),
    };
  
    try {
      const response = await fetch(`${API_URL}/auth/signup`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
  
      const data = await response.json().catch(() => ({}));
  
      if (!response.ok) {
        if (data.fieldErrors && typeof data.fieldErrors === "object") {
          Object.entries(data.fieldErrors).forEach(([field, message]) => {
            const key = field as keyof typeof payload;
            if (key in payload) {
              form.setError(key, { type: "server", message: message as string });
            }
          });
        } else if (typeof data === "object") {
          Object.entries(data).forEach(([field, message]) => {
            const key = field as keyof typeof payload;
            if (key in payload) {
              form.setError(key, { type: "server", message: message as string });
            }
          });
        } else {
          setFormError(data?.error || "Signup failed. Please try again.");
        }
        return;
      }
  
      navigate("/login");
    } catch (error: unknown) {
      console.error("Signup error:", error);
      setFormError(error instanceof Error ? error.message : "Signup failed. Please try again.");
    }
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 mx-auto py-8 max-w-lg">
        <h2 className="text-xl font-semibold text-center">Sign Up</h2>

        {formError && (
          <div className="text-red-500 text-sm text-center bg-red-50 p-2 rounded">
            {formError}
          </div>
        )}

        <div className="grid grid-cols-12 gap-4">
          <div className="col-span-6">
            <FormField
              control={form.control}
              name="username"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Username</FormLabel>
                  <FormControl>
                    <Input placeholder="Username" {...field} />
                  </FormControl>
                  <FormDescription>This is your username.</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <div className="col-span-6">
            <FormField
              control={form.control}
              name="displayName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Display Name</FormLabel>
                  <FormControl>
                    <Input placeholder="Display Name" {...field} />
                  </FormControl>
                  <FormDescription>This is your public display name.</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
        </div>

        <FormField
          control={form.control}
          name="email"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Email</FormLabel>
              <FormControl>
                <Input placeholder="email@example.com" type="email" {...field} />
              </FormControl>
              <FormDescription>This is your email.</FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />

        <div className="grid grid-cols-12 gap-4">
          <div className="col-span-6">
            <FormField
              control={form.control}
              name="password"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Password</FormLabel>
                  <FormControl>
                    <Input type="password" placeholder="Password" {...field} />
                  </FormControl>
                  <FormDescription>Enter your password.</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <div className="col-span-6">
            <FormField
              control={form.control}
              name="confirm"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Confirm Password</FormLabel>
                  <FormControl>
                    <Input type="password" placeholder="Confirm Password" {...field} />
                  </FormControl>
                  <FormDescription>Confirm your password.</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
        </div>

        <FormField
          control={form.control}
          name="isArtist"
          render={({ field }) => (
            <FormItem className="flex flex-row items-start space-x-3 space-y-0 rounded-md border p-4">
              <FormControl>
                <Checkbox checked={field.value} onCheckedChange={field.onChange} />
              </FormControl>
              <div className="space-y-1 leading-none">
                <FormLabel>Sign up as an artist?</FormLabel>
                <FormMessage />
              </div>
            </FormItem>
          )}
        />

        <Button type="submit" className="w-full">Sign Up</Button>

        <p className="text-xs cursor-pointer text-blue-500 hover:underline text-center" onClick={() => navigate("/login")}>
          Already have an account? Sign in here.
        </p>

        <p className="text-xs cursor-pointer text-blue-500 hover:underline text-center" onClick={() => navigate("/")}>
          Return home.
        </p>
      </form>
    </Form>
  );
}