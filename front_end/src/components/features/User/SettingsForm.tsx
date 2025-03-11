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
  file: z.instanceof(File).optional()
})

export default function SignupForm() {
  const {user, setUser} = useUser();
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      username: user.username,
      displayName: user.displayName,
      email: user.email,
      file: undefined,
    },
  })
  
  async function onSubmit(values: z.infer<typeof formSchema>) {
    try {
      const formData = new FormData();
      
      const userData = {
        username: values.username,
        displayName: values.displayName,
        email: values.email
      };

      formData.append("patch", new Blob([JSON.stringify(userData)], { type: "application/json" }));
      console.log(formData);

      if (values.file) {
        formData.append("file", values.file);  
      }

      const response = await fetch(`http://localhost:8080/users/update/${user.id}`, {
        mode: "cors",
        method: "PATCH",
        body: formData,
      });
  
      const data = await response.json();
  
      if (!response.ok) {
        throw new Error(data.message || "Update failed");
      }

      localStorage.setItem("user", JSON.stringify(data));
      setUser(data);
  
      console.log("Update successful:", data);
    } catch (error) {
      console.error("Update error:", error);
    }
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8  mx-auto py-8">
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
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
                    control={form.control}
                    name="file"
                    render={({ field }) => (
                        <FormItem>
                        <FormControl>
                            <Input
                            type="file"
                            accept="image/*"
                            multiple
                            onChange={(e) => {
                              const file = e.target.files ? e.target.files[0] : undefined;  
                              field.onChange(file);
                            }}
                            />
                        </FormControl>
                        <FormMessage />
                        </FormItem>
                    )}
                />
        <Button type="submit">Submit</Button>
      </form>
    </Form>
  )
}