import {
    useForm
  } from "react-hook-form"
  import {
    zodResolver
  } from "@hookform/resolvers/zod"
  import * as z from "zod"
  import {
    Button
  } from "@/components/ui/button"
  import {
    Form,
    FormControl,
    FormDescription,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
  } from "@/components/ui/form"
  import {
    Input
  } from "@/components/ui/input"
import { useUser } from "@/UserProvider"
  
   
  const formSchema = z.object({
    username: z.string().min(4).max(25),
    displayName: z.string().min(4).max(25),
    email: z.string().email(),
    password: z.string().min(8).max(30),
    confirm: z.string().min(8).max(30),
    isArtist: z.boolean().default(false).optional()
  })
  .refine((data) => data.password === data.confirm, {
      message: "Passwords don't match",
      path: ["confirm"],
  });
  
  export default function SignupForm() {
    const {user} = useUser();
      const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
          username: user.username,
          displayName: user.displayName,
          email: user.email,
          password: "",
        },
      })
     
      async function onSubmit(values: z.infer<typeof formSchema>) {
        try {
          const response = await fetch(`http://localhost:8080/users/update/${user.id}`, {
            mode: "cors",
            method: "PUT",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(values),
          });
      
          const data = await response.json();
      
          if (!response.ok) {
            throw new Error(data.message || "Update failed");
          }
      
          console.log("Update successful:", data);
        } catch (error) {
          console.error("Update error:", error);
        }
      }
  
      return (
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="w- full space-y-8  mx-auto py-8">
            <div className="grid grid-cols-12 gap-4">
              <div className="col-span-6">
                <FormField
                  control={form.control}
                  name="username"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Username</FormLabel>
                      <FormControl>
                        <Input 
                        placeholder="Username"
                        
                        type="text"
                        {...field} />
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
                        <Input 
                        placeholder="Display"
                        
                        type="text"
                        {...field} />
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
                    <Input 
                    placeholder="email@gmail.com"
                    
                    type="email"
                    {...field} />
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
                        <Input type="password" placeholder="Placeholder" {...field} />
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
                        <Input type="password" placeholder="Placeholder" {...field} />
                      </FormControl>
                      <FormDescription>Confirm your password.</FormDescription>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>
            </div>
            <Button type="submit">Submit</Button>
            <p className="text-xs">Already have an account? Sign in here.</p>
          </form>
        </Form>
      )
    }